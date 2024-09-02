package dev.nordix.services.domain.model

interface ServiceAction <out R: ServiceActionResult> {

    val identifier get() = this::class.simpleName

}