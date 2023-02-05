package com.payback.core.network

/**
 * Created By Rafiqul Hasan
 */
class RequestException(
	override var message: String = "",
) : Exception(message)
