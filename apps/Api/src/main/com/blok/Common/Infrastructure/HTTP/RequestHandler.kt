package com.blok

interface RequestHandler<V> {

    fun process(value: V, urlParams: Map<String, String>, shouldReturnHtml: Boolean): Answer

}
