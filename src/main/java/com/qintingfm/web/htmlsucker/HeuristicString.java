package com.qintingfm.web.htmlsucker;

/**
 * @author guliuzhong
 */
public class HeuristicString {

    private String string;

    public HeuristicString(String string) throws CandidateFoundException {
        this.string = string;
        if (string != null && !string.isEmpty()) {
            throw new CandidateFoundException(string);
        }
    }

    public HeuristicString or(String candidate) throws CandidateFoundException {
        if (candidate != null) {
            if (string == null || string.isEmpty()) {
                string = candidate;
            } else {
                throw new CandidateFoundException(string);
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return string;
    }

    public static class CandidateFoundException extends Exception {
        public final String candidate;

        public CandidateFoundException(String candidate) {
            this.candidate = candidate;
        }
    }
}
