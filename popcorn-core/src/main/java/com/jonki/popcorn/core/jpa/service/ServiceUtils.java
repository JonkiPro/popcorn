package com.jonki.popcorn.core.jpa.service;

import com.jonki.popcorn.common.constant.PathConstant;
import com.jonki.popcorn.common.dto.*;
import com.jonki.popcorn.common.dto.movie.*;
import com.jonki.popcorn.common.dto.movie.response.ImageResponse;
import com.jonki.popcorn.common.dto.movie.response.RateResponse;
import com.jonki.popcorn.core.jpa.entity.ContributionEntity;
import com.jonki.popcorn.core.jpa.entity.MessageEntity;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.entity.movie.*;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility methods for JPA services.
 */
public final class ServiceUtils {

    /**
     * Convert a user entity to a DTO for external exposure.
     *
     * @param userEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static User toUserDto(final UserEntity userEntity) {
        final User.Builder builder = new User.Builder(
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getFriends().size()
        )
                .withId(userEntity.getUniqueId());

        userEntity.getAvatarId()
                .map(avatarId -> builder.withAvatarSrc(userEntity.getAvatarProvider().getUrlFile(avatarId)))
                .orElseGet(() -> builder.withAvatarSrc(PathConstant.AVATAR_DEFAULT));

        return builder.build();
    }

    /**
     * Convert a contribution entity to a DTO for external exposure.
     *
     * @param contributionEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static Contribution toContributionDto(final ContributionEntity contributionEntity) {
        final Contribution.Builder builder = new Contribution.Builder(
                contributionEntity.getMovie().getId(),
                contributionEntity.getMovie().getTitle(),
                toShallowUserDto(contributionEntity.getUser()),
                contributionEntity.getStatus(),
                contributionEntity.getField(),
                contributionEntity.getSources(),
                contributionEntity.getCreated()
        )
                .withId(contributionEntity.getId().toString());

        contributionEntity.getUserComment().ifPresent(builder::withUserComment);
        contributionEntity.getVerificationDate().ifPresent(builder::withVerificationDate);
        contributionEntity.getVerificationUser().ifPresent(user ->
            builder.withVerificationUser(toShallowUserDto(user))
        );
        contributionEntity.getVerificationComment().ifPresent(builder::withVerificationComment);

        return builder.build();
    }

    /**
     * Convert a movie entity to a DTO for external exposure.
     *
     * @param movieEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static Movie toMovieDto(final MovieEntity movieEntity) {
        return initMovieDto(movieEntity).build();
    }

    /**
     * Convert a movie entity to a DTO for external exposure.
     *
     * @param movieEntity The entity to convert
     * @param userEntity The entity provides data
     * @return The immutable DTO representation of the entity data
     */
    static UserMovie toUserMovieDto(final MovieEntity movieEntity, final UserEntity userEntity) {
        final UserMovie.Builder builder = new UserMovie.Builder(initMovieDto(movieEntity).build());

        final boolean isYourRating = movieEntity.getRatings().stream().map(MovieRateEntity::getUser).collect(Collectors.toList()).contains(userEntity);
        if(isYourRating) {
            final int i = movieEntity.getRatings().stream().map(MovieRateEntity::getUser).collect(Collectors.toList()).indexOf(userEntity);
            builder.withYourRating((float) movieEntity.getRatings().stream().map(MovieRateEntity::getRate).collect(Collectors.toList()).get(i));
        }
        builder.withFavorited(userEntity.getFavoritesMovies().contains(movieEntity));

        return builder.build();
    }

    /**
     * Get and initialization of the movie's builder.
     *
     * @param movieEntity Movie entity to obtain data
     * @return The builder of Movie DTO
     */
    private static Movie.Builder initMovieDto(final MovieEntity movieEntity) {
        final Movie.Builder builder = (Movie.Builder) new Movie.Builder(
                movieEntity.getTitle(),
                movieEntity.getType()
        )
                .withId(movieEntity.getId().toString());

        movieEntity.getRating().ifPresent(builder::withRating);
        movieEntity.getRating().ifPresent(ratings -> builder.withNumberOfRating(movieEntity.getRatings().size()));
        final Optional<String> locatedTitle = movieEntity.getOtherTitles().stream()
                .filter(ot -> ot.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toOtherTitleDto)
                .filter(ot -> ot.getCountry() != null
                        && ot.getCountry().getCode().equals(Locale.getDefault().getCountry()))
                .map(OtherTitle::getTitle)
                .findFirst();
        locatedTitle.ifPresent(builder::withTitleLocated);
        final Supplier<Stream<ReleaseDate>> releaseDateStream = () -> movieEntity.getReleaseDates().stream()
                .filter(ot -> ot.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toReleaseDateDto);
        Optional<ReleaseDate> releaseDate = releaseDateStream.get()
                .filter(rd -> rd.getCountry().getCode().equals(Locale.getDefault().getCountry()))
                .findFirst();
        if(releaseDate.isPresent()) {
            builder.withReleaseDate(releaseDate.get());
        } else {
            releaseDate = releaseDateStream.get()
                    .filter(rd -> movieEntity.getCountries().stream()
                            .filter(c -> c.getStatus() == DataStatus.ACCEPTED)
                            .map(ServiceUtils::toCountryDto)
                            .collect(Collectors.toList()).stream()
                            .map(Country::getCountry)
                            .collect(Collectors.toList())
                            .contains(rd.getCountry()))
                    .findFirst();
            if(releaseDate.isPresent()) {
                builder.withReleaseDate(releaseDate.get());
            } else {
                releaseDate = releaseDateStream.get().findFirst();
                builder.withReleaseDate(releaseDate.orElse(null));
            }
        }
        builder.withCountries(movieEntity.getCountries().stream().filter(c -> c.getStatus() == DataStatus.ACCEPTED).map(MovieCountryEntity::getCountry).collect(Collectors.toList()));
        builder.withLanguages(movieEntity.getLanguages().stream().filter(l -> l.getStatus() == DataStatus.ACCEPTED).map(MovieLanguageEntity::getLanguage).collect(Collectors.toList()));
        builder.withGenres(movieEntity.getGenres().stream().filter(g -> g.getStatus() == DataStatus.ACCEPTED).map(MovieGenreEntity::getGenre).collect(Collectors.toList()));
        builder.withBoxofficeCumulative(movieEntity.getBoxOffices().stream().filter(bo -> bo.getStatus() == DataStatus.ACCEPTED).map(MovieBoxOfficeEntity::getBoxOffice).reduce(BigDecimal::add).orElse(null));
        builder.withOutline(movieEntity.getOutlines().stream().filter(o -> o.getStatus() == DataStatus.ACCEPTED).map(MovieOutlineEntity::getOutline).findFirst().orElse(null));
        builder.withSummary(movieEntity.getSummaries().stream().filter(s -> s.getStatus() == DataStatus.ACCEPTED).map(MovieSummaryEntity::getSummary).findFirst().orElse(null));
        return builder;
    }

    /**
     * Convert a message entity to a DTO for external exposure.
     *
     * @param messageEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static MessageSent toMessageSentDto(final MessageEntity messageEntity) {
        final MessageSent.Builder builder = new MessageSent.Builder(
                messageEntity.getSubject(),
                messageEntity.getText(),
                messageEntity.getCreated(),
                toShallowUserDto(messageEntity.getRecipient())
        )
                .withId(messageEntity.getUniqueId());

        return builder.build();
    }

    /**
     * Convert a message entity to a DTO for external exposure.
     *
     * @param messageEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static MessageReceived toMessageReceivedDto(final MessageEntity messageEntity) {
        final MessageReceived.Builder builder = new MessageReceived.Builder(
                messageEntity.getSubject(),
                messageEntity.getText(),
                messageEntity.getCreated(),
                toShallowUserDto(messageEntity.getSender()),
                messageEntity.getDateOfRead()
        )
                .withId(messageEntity.getUniqueId());

        return builder.build();
    }

    /**
     * Convert a user entity to a DTO for external exposure.
     *
     * @param userEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static ShallowUser toShallowUserDto(final UserEntity userEntity) {
        final ShallowUser.Builder builder = new ShallowUser.Builder(
                userEntity.getUsername(),
                userEntity.getAvatarId()
                        .map(avatarId -> userEntity.getAvatarProvider().getUrlFile(avatarId))
                        .orElse(PathConstant.AVATAR_DEFAULT)
        )
                .withId(userEntity.getUniqueId());

        return builder.build();
    }

    /**
     * Convert a MovieOtherTitleEntity entity to a DTO for external exposure.
     *
     * @param movieOtherTitleEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static OtherTitle toOtherTitleDto(final MovieOtherTitleEntity movieOtherTitleEntity) {
        final OtherTitle.Builder builder = new OtherTitle.Builder(
                movieOtherTitleEntity.getTitle(),
                movieOtherTitleEntity.getCountry()
        );

        Optional.ofNullable(movieOtherTitleEntity.getAttribute()).ifPresent(builder::withAttribute);

        return builder.build();
    }

    /**
     * Convert a MovieReleaseDateEntity entity to a DTO for external exposure.
     *
     * @param movieReleaseDateEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static ReleaseDate toReleaseDateDto(final MovieReleaseDateEntity movieReleaseDateEntity) {
        final ReleaseDate.Builder builder = new ReleaseDate.Builder(
                movieReleaseDateEntity.getDate(),
                movieReleaseDateEntity.getCountry()
        );

        return builder.build();
    }

    /**
     * Convert a MovieOutlineEntity entity to a DTO for external exposure.
     *
     * @param movieOutlineEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static Outline toOutlineDto(final MovieOutlineEntity movieOutlineEntity) {
        final Outline.Builder builder = new Outline.Builder(
                movieOutlineEntity.getOutline()
        );

        return builder.build();
    }

    /**
     * Convert a MovieSummaryEntity entity to a DTO for external exposure.
     *
     * @param movieSummaryEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static Summary toSummaryDto(final MovieSummaryEntity movieSummaryEntity) {
        final Summary.Builder builder = new Summary.Builder(
                movieSummaryEntity.getSummary()
        );

        return builder.build();
    }

    /**
     * Convert a MovieSynopsisEntity entity to a DTO for external exposure.
     *
     * @param movieSynopsisEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static Synopsis toSynopsisDto(final MovieSynopsisEntity movieSynopsisEntity) {
        final Synopsis.Builder builder = new Synopsis.Builder(
                movieSynopsisEntity.getSynopsis()
        );

        return builder.build();
    }

    /**
     * Convert a MovieBoxOfficeEntity entity to a DTO for external exposure.
     *
     * @param movieBoxOfficeEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static BoxOffice toBoxOfficeDto(final MovieBoxOfficeEntity movieBoxOfficeEntity) {
        final BoxOffice.Builder builder = new BoxOffice.Builder(
                movieBoxOfficeEntity.getBoxOffice(),
                movieBoxOfficeEntity.getCountry()
        );

        return builder.build();
    }

    /**
     * Convert a MovieSiteEntity entity to a DTO for external exposure.
     *
     * @param movieSiteEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static Site toSiteDto(final MovieSiteEntity movieSiteEntity) {
        final Site.Builder builder = new Site.Builder(
                movieSiteEntity.getSite(),
                movieSiteEntity.getOfficial()
        );

        return builder.build();
    }

    /**
     * Convert a MovieCountryEntity entity to a DTO for external exposure.
     *
     * @param movieCountryEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static Country toCountryDto(final MovieCountryEntity movieCountryEntity) {
        final Country.Builder builder = new Country.Builder(
                movieCountryEntity.getCountry()
        );

        return builder.build();
    }

    /**
     * Convert a MovieLanguageEntity entity to a DTO for external exposure.
     *
     * @param movieLanguageEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static Language toLanguageDto(final MovieLanguageEntity movieLanguageEntity) {
        final Language.Builder builder = new Language.Builder(
                movieLanguageEntity.getLanguage()
        );

        return builder.build();
    }

    /**
     * Convert a MovieGenreEntity entity to a DTO for external exposure.
     *
     * @param movieGenreEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static Genre toGenreDto(final MovieGenreEntity movieGenreEntity) {
        final Genre.Builder builder = new Genre.Builder(
                movieGenreEntity.getGenre()
        );

        return builder.build();
    }

    /**
     * Convert a MovieReviewEntity entity to a DTO for external exposure.
     *
     * @param movieReviewEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static Review toReviewDto(final MovieReviewEntity movieReviewEntity) {
        final Review.Builder builder = new Review.Builder(
                movieReviewEntity.getTitle(),
                movieReviewEntity.getReview(),
                movieReviewEntity.isSpoiler()
        );

        return builder.build();
    }

    /**
     * Convert a MoviePhotoEntity entity to a DTO for external exposure.
     *
     * @param moviePhotoEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static ImageResponse toImageResponseDto(final MoviePhotoEntity moviePhotoEntity) {
        final ImageResponse.Builder builder = new ImageResponse.Builder(
                moviePhotoEntity.getProvider().getUrlFile(moviePhotoEntity.getIdInCloud())
        );

        return builder.build();
    }

    /**
     * Convert a MoviePosterEntity entity to a DTO for external exposure.
     *
     * @param moviePosterEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static ImageResponse toImageResponseDto(final MoviePosterEntity moviePosterEntity) {
        final ImageResponse.Builder builder = new ImageResponse.Builder(
                moviePosterEntity.getProvider().getUrlFile(moviePosterEntity.getIdInCloud())
        );

        return builder.build();
    }

    /**
     * Convert a MovieRateEntity entity to a DTO for external exposure.
     *
     * @param movieRateEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static RateResponse toRateResponseDto(final MovieRateEntity movieRateEntity) {
        final RateResponse.Builder builder = new RateResponse.Builder(
                movieRateEntity.getRate(),
                movieRateEntity.getUser().getUsername(),
                movieRateEntity.getDate()
        );

        return builder.build();
    }
}
