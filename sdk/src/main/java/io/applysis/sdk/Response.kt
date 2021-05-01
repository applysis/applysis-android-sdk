package io.applysis.sdk

sealed class ApplysisError {
    /**
     * Indicates one of the errors: API Key not correct or being invalid
     */
    object Forbidden : ApplysisError()
    object BadRequest : ApplysisError()
    object NonValid : ApplysisError()
    data class Unknown(val error: Throwable? = null) : ApplysisError()
}


sealed class FeedbackResult {
    object Success : FeedbackResult()
    data class Error(val error: ApplysisError) : FeedbackResult()
}
