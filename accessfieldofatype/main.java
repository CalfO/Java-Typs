
public static void main(String[] args){
	getAllDateFieldNameOgDevice();
}

/**
 * Utility method to access all field of type Date of any complex object as deep as he can.
 * Just change first line by corresponding class.
 * And use @Test to run it easily
 */
// @Test
public void getAllDateFieldNameOgDevice() {
	Field[] deviceFields = Device.class.getDeclaredFields();
	Set<String> dateFieldName = getDateFieldName(deviceFields, "root", Date.class);
	dateFieldName.stream().forEach(dfn -> System.out.print("'"+dfn+"',"));
}

private Set<String> getDateFieldName(Field[] deviceFields, String fatherField, Class<?> classToFind) {
	Set<String> dateFieldName = new HashSet<>();
	for (Field field : deviceFields) {
		Field[] subField = field.getType().getDeclaredFields();
		if (subField != null && Arrays.stream(subField).anyMatch(sb -> sb.getType().equals(classToFind))) {
			dateFieldName.addAll(getDateFieldName(subField, fatherField + "." + field.getName(), classToFind));
		} else {
			if (field.getType().equals(classToFind)) {
				String fieldName = field.getName();
				dateFieldName.add(fatherField +"."+ fieldName);
				System.out.println("Date field = " + fatherField +"."+ fieldName);
			}
		}
	}
	return dateFieldName;
}