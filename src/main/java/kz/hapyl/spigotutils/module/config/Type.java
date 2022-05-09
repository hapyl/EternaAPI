package kz.hapyl.spigotutils.module.config;

public enum Type {

    GENERIC(null),
    UUID(new Convert() {
        @Override
        public Object load(Object obj) {
            return java.util.UUID.fromString((String) obj);
        }

        @Override
        public Object save(Object obj) {
            return obj.toString();
        }
    });

    private final Convert convert;

    Type(Convert convert) {
        this.convert = convert;
    }

    public boolean isGeneric() {
        return this.convert == null;
    }

    public Object convertLoad(Object obj) {
        if (isGeneric()) {
            return obj;
        }
        return convert.load(obj);
    }

    public Object convertSave(Object obj) {
        if (isGeneric()) {
            return obj;
        }
        return convert.save(obj);
    }
}
