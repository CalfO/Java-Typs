
public static void main(String[] args){
	getAllDateFieldNameOgDevice();
}

/**
 * Java 8
 * Utility method to access all field of type Date of any complex object as deep as he can.
 * Just change first line by corresponding class.
 * And use @Test to run it easily
 */
public void getAllDateFieldNameOgDevice() {
	Field[] deviceFields = MyObject.class.getDeclaredFields();
	List<String> dateFieldName = getDateFieldName(deviceFields, "root", Date.class);
	dateFieldName.stream().forEach(dfn -> System.out.print("'"+dfn+"',"));
}

private List<String> getDateFieldName(Field[] deviceFields, String fatherField, Class<?> classToFind) {
	List<String> dateFieldName = new ArrayList<>();
	for (Field field : deviceFields) {
		Field[] subField = field.getType().getDeclaredFields();
		if (subField != null && Arrays.stream(subField).anyMatch(sb -> Objects.equals(sb.getType() ,classToFind))) {
			dateFieldName.addAll(getDateFieldName(subField, fatherField + "." + field.getName(), classToFind));
		} else {
			if (Objects.equals(field.getType(), classToFind)) {
				String fieldName = field.getName();
				dateFieldName.add(fatherField +"."+ fieldName);
				System.out.println("Date field = " + fatherField +"."+ fieldName);
			}
		}
	}
	return dateFieldName;
}