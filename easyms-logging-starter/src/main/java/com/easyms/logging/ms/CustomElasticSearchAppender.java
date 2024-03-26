package com.easyms.logging.ms;

import com.agido.logback.elasticsearch.ClassicElasticsearchPublisher;
import com.agido.logback.elasticsearch.config.Settings;
import com.internetitem.logback.elasticsearch.ElasticsearchAppender;

import java.io.IOException;

public class CustomElasticSearchAppender extends ElasticsearchAppender {

    public CustomElasticSearchAppender() {
    }

    public CustomElasticSearchAppender(Settings settings) {
        super(settings);
    }

    @Override
    protected ClassicElasticsearchPublisher buildElasticsearchPublisher() throws IOException {
        return new CustomClassicElasticSearchPublisher(this.getContext(), this.errorReporter, this.settings, this.elasticsearchProperties, this.headers);
    }

    public Settings getSettings() {
        return super.settings;
    }

}
