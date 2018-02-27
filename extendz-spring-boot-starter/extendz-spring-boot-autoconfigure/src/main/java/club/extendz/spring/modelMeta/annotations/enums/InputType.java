package club.extendz.spring.modelMeta.annotations.enums;

public enum InputType {
	STRING("string"), NUMBER("number"), PASSWORD("password"), EMAIL("email"),DATE("date"),TIME_STAMP("timestamp"),BOOLEAN("boolean");

	private String type;

	private InputType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return this.type;
	}
}
