package foo.bar.clean.domain.utils

import foo.bar.clean.domain.services.api.FeatureFlag
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapEntrySerializer
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object MapStringStringDeserializer: KSerializer<Map<String, String>> {

    private val mapSerializer = ListSerializer(MapEntrySerializer(String.serializer(), String.serializer()))
    override val descriptor: SerialDescriptor = mapSerializer.descriptor

    override fun deserialize(decoder: Decoder): Map<String, String> {
        return mapSerializer.deserialize(decoder).associate { it.toPair() }
    }

    override fun serialize(encoder: Encoder, value: Map<String, String>) {
        mapSerializer.serialize(encoder, value.entries.toList())
    }
}

object MapStringBooleanDeserializer: KSerializer<Map<String, Boolean>> {

    private val mapSerializer = ListSerializer(MapEntrySerializer(String.serializer(), Boolean.serializer()))
    override val descriptor: SerialDescriptor = mapSerializer.descriptor

    override fun deserialize(decoder: Decoder): Map<String, Boolean> {
        return mapSerializer.deserialize(decoder).associate { it.toPair() }
    }

    override fun serialize(encoder: Encoder, value: Map<String, Boolean>) {
        mapSerializer.serialize(encoder, value.entries.toList())
    }
}

object MapFeatureFlagBooleanDeserializer: KSerializer<Map<FeatureFlag, Boolean>> {

    private val mapSerializer = ListSerializer(PairSerializer(FeatureFlag.serializer(), Boolean.serializer()))
    override val descriptor: SerialDescriptor = mapSerializer.descriptor

    override fun deserialize(decoder: Decoder): Map<FeatureFlag, Boolean> {
        return mapSerializer.deserialize(decoder).associate { it }
    }

    override fun serialize(encoder: Encoder, value: Map<FeatureFlag, Boolean>) {
        mapSerializer.serialize(encoder, value.entries.toList().map { it.toPair() })
    }
}
