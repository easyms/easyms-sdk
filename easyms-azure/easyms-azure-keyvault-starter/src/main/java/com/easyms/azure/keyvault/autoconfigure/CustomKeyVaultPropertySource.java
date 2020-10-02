
package com.easyms.azure.keyvault.autoconfigure;

import com.microsoft.azure.keyvault.spring.KeyVaultOperation;
import org.springframework.core.env.EnumerablePropertySource;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CustomKeyVaultPropertySource extends EnumerablePropertySource<KeyVaultOperation> {
    private final KeyVaultOperation operations;

    private static Map<String, Optional<Object>> cache = new ConcurrentHashMap<>();

    public CustomKeyVaultPropertySource(KeyVaultOperation operation) {
        super("azurekvcached", operation);
        this.operations = operation;
    }

    public String[] getPropertyNames() {
        return this.operations.list();
    }

    public Object getProperty(String name) {

        Optional<Object> value = cache.get(name);
        if(value == null) {
            Object objValue = this.operations.get(name);
            value = Optional.ofNullable(objValue);
            cache.put(name, value);
        }

        return value.orElse(null);
    }
}
