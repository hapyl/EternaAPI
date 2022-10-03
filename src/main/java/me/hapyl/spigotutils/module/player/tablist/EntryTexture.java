package me.hapyl.spigotutils.module.player.tablist;

public enum EntryTexture {

    GRAY(
            "IBS0+YYdMqchhsMtzI4ZWZoaM6oQhEAkGJFGxDeqKEGsCWyVVYjbOY3s6N9x1k8ECAO3pcyoOpLtG+RQ2d1gBsQpHtBmZTzpKOzsLLK+SGWS1z7TlonAtmhfGep7gxJFMmNIhxczs+Ybi1hyxOJqSqGkLhP+VqA5CscOV+Et10W11TKQL1UidUVhfaJHv6Yp7jho14tsdCD295gC8N98Pi2slldh61+2jMQiFi1XrU+BZbg5B47VLN8cpaN1+rk+JvcXI+9FtSik8a/p7HoVfBIR1CBQWYFLIRY3aP8G/gVQqwnoC4yoBA9P+uoEJqLUGvhRGe60a0n62FxbW4B9zHuW5SL+dXbb5vRgp/8woHw2JE7aB5OMzLSKGKzxged03kA382KMhrtj4cZ7P5RKQHSCV7IQ1w9K0GcOOfyVQAJ+wv8N19zVonyrhwjK03Fe1wKHFXAxgL6Qn9qdyE5L6PPbp5wf9w+Pm+AbCjLixha3wDn4+bN6dvkDYoajXmbX1s2WwEihnVQrBtk90koyZTh/LYyakkUFmQsder5mhJcz/tVgidCLk6PEreanGKXq84qwJK4v1uFumcWAnWoQmdk4uA0bnGvG7c8K4BvNNZitMCVTlPZzkCuZ3PM1U1iKtS6TWB2RSLB7tQNrF70QAxITguIUQyIfaJmL9u+BDIE=",
            "ScFX2rWxyhSyIn3YIke0ecNezt0bx+K8fRLQVPAzNli+X/dmod9ohujwwmDyog0exnlDAdEtXJY2XEUELpNLBHYZFyMsUChL4MObACzxGXExRBbyE8NzemmcRePKmglJfIHBxATlvG3VXeNn1dNA9g+GgLAB4KdqlDaYO5qAtdET7rckzLhSVeFz3yct3LuqZwzutdkJIbnR2Bu9kM4IpyowDbBEoASUp2ogNq4bQ+9O7cU7PtayknPGJaustHQR32jVcLYNqGweZKjZZUgER+6XrGAwyuWENQ00UpEWanHa2ahBugxzg1atcgc3spx3FxWLN0bVsUj4oXulPhxD4/44jALhHl7898qXwoRhqGaAuombJRH/bMoyoTZUDgcxmTbWFZcos9Ugg6eBlQo7ip35mW7fyd/8rk6RCGyf/wmqyDUnFNUHeQgJHHED+yr2oN/Y4jzCmG5Ikk1RExpi2Mbi7ouZx3bKzkTsEafGdnx8sMxenRCYFtcoQCcV4woQsk7xqqJyBVFiU4wXzHzMnbOhiRPJHlcqzJljFw2LuI97f8vlQGpW5KriFUWLSgKs1Zw4QOIdl5cv/oSZOvEAoi0s5EOB4rzVVE1XXLatYWOaRXy6Nbl8c9SH8UbkhcK5t+J54UIsvvuqq8AoDOX6yyw/wDORVlTaqy7pVXKZSQk="
    );


    private final String value;
    private final String signature;

    EntryTexture(String value, String signature) {
        this.value = value;
        this.signature = signature;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }
}
