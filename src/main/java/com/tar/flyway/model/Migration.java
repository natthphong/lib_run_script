package com.tar.flyway.model;

public class Migration {
    private String version;
    private String description;
    private String script;

    public Migration(String version, String description, String script) {
        this.version = version;
        this.description = description;
        this.script = script;
    }
    public Migration(){}

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
