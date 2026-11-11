package com.example.locomotive.util;

import java.util.Optional;

/**
 * Holds either a success value or a failure value. Compatible with Java 11.
 *
 * Exactly one of the two is ever set. Use {@link #of(Object, Object)} when you have
 * two nullable values (e.g. from a protobuf oneof) and want the Result to work out
 * which side is present.
 *
 * @param <S> the success type
 * @param <F> the failure type
 */
public final class Result<S, F> {

    private final S success;
    private final F failure;
    private final boolean isSuccess;

    private Result(S success, F failure, boolean isSuccess) {
        this.success = success;
        this.failure = failure;
        this.isSuccess = isSuccess;
    }

    public static <S, F> Result<S, F> success(S value) {
        return new Result<>(value, null, true);
    }

    public static <S, F> Result<S, F> failure(F error) {
        return new Result<>(null, error, false);
    }

    /**
     * Builds a Result from two nullable values, where exactly one must be non-null.
     * The non-null side determines whether this is a success or a failure.
     *
     * @throws IllegalArgumentException if both are null or both are non-null
     */
    public static <S, F> Result<S, F> of(S success, F failure) {
        if ((success == null) == (failure == null)) {
            throw new IllegalArgumentException("Exactly one of success or failure must be set");
        }
        return failure != null
                ? Result.<S, F>failure(failure)
                : Result.<S, F>success(success);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFailure() {
        return !isSuccess;
    }

    /** The success value, or empty if this is a failure (or the value is null). */
    public Optional<S> getSuccess() {
        return Optional.ofNullable(success);
    }

    /** The failure value, or empty if this is a success (or the value is null). */
    public Optional<F> getFailure() {
        return Optional.ofNullable(failure);
    }
}