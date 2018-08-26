package com.jonki.popcorn.core.jpa.specification;

import com.jonki.popcorn.core.jpa.entity.MessageEntity;
import com.jonki.popcorn.core.jpa.entity.MessageEntity_;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Tests for the message specifications.
 */
@Category(UnitTest.class)
public class MessageSpecsUnitTests {

    private static final UserEntity USER = Mockito.mock(UserEntity.class);
    private static final String SUBJECT = "Subject";
    private static final String TEXT = "Text";

    private Root<MessageEntity> root;
    private CriteriaQuery<?> cq;
    private CriteriaBuilder cb;

    /**
     * Setup some variables.
     */
    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        this.root = (Root<MessageEntity>) Mockito.mock(Root.class);
        this.cb = Mockito.mock(CriteriaBuilder.class);

        final Path<UserEntity> userPath = (Path<UserEntity>) Mockito.mock(Path.class);
        final Predicate equalUserPredicate = Mockito.mock(Predicate.class);
        Mockito.when(this.root.get(MessageEntity_.sender)).thenReturn(userPath);
        Mockito.when(this.root.get(MessageEntity_.recipient)).thenReturn(userPath);
        Mockito.when(this.cb.equal(userPath, USER)).thenReturn(equalUserPredicate);

        final Path<String> subjectPath = (Path<String>) Mockito.mock(Path.class);
        final Predicate likeSubjectPredicate = Mockito.mock(Predicate.class);
        Mockito.when(this.root.get(MessageEntity_.subject)).thenReturn(subjectPath);
        Mockito.when(this.cb.like(subjectPath, SUBJECT)).thenReturn(likeSubjectPredicate);

        final Path<String> textPath = (Path<String>) Mockito.mock(Path.class);
        final Predicate likeTextPredicate = Mockito.mock(Predicate.class);
        Mockito.when(this.root.get(MessageEntity_.text)).thenReturn(textPath);
        Mockito.when(this.cb.like(textPath, TEXT)).thenReturn(likeTextPredicate);

        final Path<Boolean> isVisibleForSenderPath = (Path<Boolean>) Mockito.mock(Path.class);
        final Predicate isTrueIsVisibleForSenderPredicate = Mockito.mock(Predicate.class);
        Mockito.when(this.root.get(MessageEntity_.isVisibleForSender)).thenReturn(isVisibleForSenderPath);
        Mockito.when(this.cb.isTrue(isVisibleForSenderPath)).thenReturn(isTrueIsVisibleForSenderPredicate);

        final Path<Boolean> isVisibleForRecipientPath = (Path<Boolean>) Mockito.mock(Path.class);
        final Predicate isTrueIsVisibleForRecipientPredicate = Mockito.mock(Predicate.class);
        Mockito.when(this.root.get(MessageEntity_.isVisibleForRecipient)).thenReturn(isVisibleForRecipientPath);
        Mockito.when(this.cb.isTrue(isVisibleForRecipientPath)).thenReturn(isTrueIsVisibleForRecipientPredicate);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindAllSentMessagesForUser() {
        final Specification<MessageEntity> spec = MessageSpecs.findSentMessagesForUser(USER, SUBJECT, TEXT);

        spec.toPredicate(this.root, this.cq, this.cb);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(MessageEntity_.sender), USER);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .like(this.cb.upper(this.root.get(MessageEntity_.subject)), "%" + SUBJECT.toUpperCase() + "%");
        Mockito
                .verify(this.cb, Mockito.times(1))
                .like(this.cb.upper(this.root.get(MessageEntity_.text)), "%" + TEXT.toUpperCase() + "%");
        Mockito
                .verify(this.cb, Mockito.times(1))
                .isTrue(this.root.get(MessageEntity_.isVisibleForSender));
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindAllSentMessagesForUserWithOutSubject() {
        final Specification<MessageEntity> spec = MessageSpecs.findSentMessagesForUser(USER, null, TEXT);

        spec.toPredicate(this.root, this.cq, this.cb);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(MessageEntity_.sender), USER);
        Mockito
                .verify(this.cb, Mockito.never())
                .like(this.cb.upper(this.root.get(MessageEntity_.subject)), "%" + SUBJECT.toUpperCase() + "%");
        Mockito
                .verify(this.cb, Mockito.times(1))
                .like(this.cb.upper(this.root.get(MessageEntity_.text)), "%" + TEXT.toUpperCase() + "%");
        Mockito
                .verify(this.cb, Mockito.times(1))
                .isTrue(this.root.get(MessageEntity_.isVisibleForSender));
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindAllSentMessagesForUserWithOutText() {
        final Specification<MessageEntity> spec = MessageSpecs.findSentMessagesForUser(USER, SUBJECT, null);

        spec.toPredicate(this.root, this.cq, this.cb);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(MessageEntity_.sender), USER);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .like(this.cb.upper(this.root.get(MessageEntity_.subject)), "%" + SUBJECT.toUpperCase() + "%");
        Mockito
                .verify(this.cb, Mockito.never())
                .like(this.cb.upper(this.root.get(MessageEntity_.text)), "%" + TEXT.toUpperCase() + "%");
        Mockito
                .verify(this.cb, Mockito.times(1))
                .isTrue(this.root.get(MessageEntity_.isVisibleForSender));
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindAllReceivedMessagesForUser() {
        final Specification<MessageEntity> spec = MessageSpecs.findReceivedMessagesForUser(USER, SUBJECT, TEXT);

        spec.toPredicate(this.root, this.cq, this.cb);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(MessageEntity_.recipient), USER);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .like(this.cb.upper(this.root.get(MessageEntity_.subject)), "%" + SUBJECT.toUpperCase() + "%");
        Mockito
                .verify(this.cb, Mockito.times(1))
                .like(this.cb.upper(this.root.get(MessageEntity_.text)), "%" + TEXT.toUpperCase() + "%");
        Mockito
                .verify(this.cb, Mockito.times(1))
                .isTrue(this.root.get(MessageEntity_.isVisibleForRecipient));
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindAllReceivedMessagesForUserWithOutSubject() {
        final Specification<MessageEntity> spec = MessageSpecs.findReceivedMessagesForUser(USER, null, TEXT);

        spec.toPredicate(this.root, this.cq, this.cb);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(MessageEntity_.recipient), USER);
        Mockito
                .verify(this.cb, Mockito.never())
                .like(this.cb.upper(this.root.get(MessageEntity_.subject)), "%" + SUBJECT.toUpperCase() + "%");
        Mockito
                .verify(this.cb, Mockito.times(1))
                .like(this.cb.upper(this.root.get(MessageEntity_.text)), "%" + TEXT.toUpperCase() + "%");
        Mockito
                .verify(this.cb, Mockito.times(1))
                .isTrue(this.root.get(MessageEntity_.isVisibleForRecipient));
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindAllReceivedMessagesForUserWithOutText() {
        final Specification<MessageEntity> spec = MessageSpecs.findReceivedMessagesForUser(USER, SUBJECT, null);

        spec.toPredicate(this.root, this.cq, this.cb);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(MessageEntity_.recipient), USER);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .like(this.cb.upper(this.root.get(MessageEntity_.subject)), "%" + SUBJECT.toUpperCase() + "%");
        Mockito
                .verify(this.cb, Mockito.never())
                .like(this.cb.upper(this.root.get(MessageEntity_.text)), "%" + TEXT.toUpperCase() + "%");
        Mockito
                .verify(this.cb, Mockito.times(1))
                .isTrue(this.root.get(MessageEntity_.isVisibleForRecipient));
    }
}
