package com.core.movie;

/**
 * Possible statuses for verification.
 */
public enum VerificationStatus {

    /*
    //  Accept the data
     */
    ACCEPT {
        @Override
        public DataStatus getDataStatus() {
            return DataStatus.ACCEPTED;
        }
    },
    /*
    //  Reject the data
     */
    REJECT {
        @Override
        public DataStatus getDataStatus() {
            return DataStatus.REJECTED;
        }
    };

    /**
     * Get an alternative status from DataStatus.
     *
     * @return The data status
     */
    public abstract DataStatus getDataStatus();
}
