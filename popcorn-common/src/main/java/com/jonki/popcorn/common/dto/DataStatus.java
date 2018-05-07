package com.jonki.popcorn.common.dto;

/**
 * Data status.
 */
public enum DataStatus {

    /**
     * The data is waiting for verification
     */
    WAITING,

    /**
     * The data was accepted by the verifier
     */
    ACCEPTED,

    /**
     * The data was rejected by the verifier
     */
    REJECTED,

    /**
     * Notification of data replacement with new data has been accepted by the verifier
     */
    EDITED,

    /**
     * The data deletion notification has been accepted by the verifier
     */
    DELETED,

    /**
     * The notification of correcting data with new data has been accepted by the verifier
     */
    AMENDMENT_ACCEPTED
}
