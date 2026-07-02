package com.bean.deserializer;

import com.bean.model.LCA;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class LcaDeserializer extends StdDeserializer<LCA> {

    public LcaDeserializer() {
        super(LCA.class);
    }

    @Override
    public LCA deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        // Accept plain number: "lca": 1
        if (p.currentToken() == JsonToken.VALUE_NUMBER_INT) {
            long id = p.getLongValue();
            if (id == 0) return null;
            LCA lca = new LCA();
            lca.setLcaId(id);
            return lca;
        }
        // Accept null or "lca": null
        if (p.currentToken() == JsonToken.VALUE_NULL) {
            return null;
        }
        // Accept object: "lca": {"lcaId": 1}
        if (p.currentToken() == JsonToken.START_OBJECT) {
            return p.readValueAs(LCA.class);
        }
        return null;
    }
}
