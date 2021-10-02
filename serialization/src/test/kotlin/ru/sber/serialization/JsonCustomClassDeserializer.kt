package ru.sber.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JsonCustomClassDeserializer {

    @Test
    fun `Нобходимо десериализовать данные в класс`() {
        // given
        val data = """{"client": "Иванов Иван Иванович"}"""
        val objectMapper = ObjectMapper()
            .registerModules(KotlinModule(),
                SimpleModule().addDeserializer(Client7::class.java, CustomClassDeserializer()))

        // when
        val client = objectMapper.readValue<Client7>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
    }
}

class CustomClassDeserializer: JsonDeserializer<Client7>() {
    override fun deserialize(p0: JsonParser?, p1: DeserializationContext?): Client7 {
       val client = p0?.readValueAsTree<ObjectNode>()?.get("client").toString()
        val names = client.replace("\"", "").split(" ")
        return Client7(firstName = names.component2(),
        lastName = names.component1(),
        middleName = names.component3())
    }


}