package com.core.jpa.service;

import com.common.dto.*;
import com.common.dto.movie.*;
import com.common.dto.movie.response.RateResponse;
import com.core.jpa.entity.ContributionEntity;
import com.core.jpa.entity.MessageEntity;
import com.core.jpa.entity.MovieEntity;
import com.core.jpa.entity.UserEntity;
import com.core.jpa.entity.movie.*;

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
                contributionEntity.getUser().getUsername(),
                contributionEntity.getStatus(),
                contributionEntity.getField(),
                contributionEntity.getSources(),
                contributionEntity.getCreated()
        )
                .withId(contributionEntity.getId().toString());

        contributionEntity.getUserComment().ifPresent(builder::withUserComment);
        contributionEntity.getVerificationDate().ifPresent(builder::withVerificationDate);
        contributionEntity.getVerificationUser().ifPresent(user ->
            builder.withVerificationUsername(user.getUsername())
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
        final Movie.Builder builder = new Movie.Builder(
                movieEntity.getTitle(),
                movieEntity.getType()
        )
                .withId(movieEntity.getId().toString());

        return builder.build();
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
                messageEntity.getRecipient().getUsername()
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
                messageEntity.getSender().getUsername(),
                messageEntity.getDateOfRead()
        )
                .withId(messageEntity.getUniqueId());

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
     * Convert a MovieStorylineEntity entity to a DTO for external exposure.
     *
     * @param movieStorylineEntity The entity to convert
     * @return The immutable DTO representation of the entity data
     */
    static Storyline toStorylineDto(final MovieStorylineEntity movieStorylineEntity) {
        final Storyline.Builder builder = new Storyline.Builder(
                movieStorylineEntity.getStoryline()
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
                movieReviewEntity.getReview()
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
