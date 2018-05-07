package com.jonki.popcorn.core.jpa.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

/**
 * Representation of the message's metamodel.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(MessageEntity.class)
public abstract class MessageEntity_ {
    public static volatile SingularAttribute<MessageEntity, Long> id;
    public static volatile SingularAttribute<MessageEntity, UserEntity> sender;
    public static volatile SingularAttribute<MessageEntity, UserEntity> recipient;
    public static volatile SingularAttribute<MessageEntity, String> subject;
    public static volatile SingularAttribute<MessageEntity, String> text;
    public static volatile SingularAttribute<MessageEntity, Date> date;
    public static volatile SingularAttribute<MessageEntity, Date> dateOfRead;
    public static volatile SingularAttribute<MessageEntity, Boolean> isVisibleForSender;
    public static volatile SingularAttribute<MessageEntity, Boolean> isVisibleForRecipient;
}
