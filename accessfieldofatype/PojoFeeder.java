@Slf4j
public class PojoFeeder {

	private Map<String, List<Object>> injectors;

	public PojoFeeder(Map<String, List<Object>> injectors) {
		this.injectors = injectors;
	}

	public <T> T generatePojoFed(Class<T> clazz) throws InstanceCreationException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		return generatePojoFed(clazz, clazz.getName());
	}

	public <T> T generatePojoFed(Class<T> clazz, String luceneParentName) throws InstanceCreationException,
			IllegalAccessException, InvocationTargetException, InstantiationException {

		Optional<T> pojoFedOpt = newInstance(clazz);
		if (!pojoFedOpt.isPresent()) {
      // TODO Create InstanceCreationException in proper exception folder
			throw new InstanceCreationException("Creation of new instance failed for class: " + clazz.getCanonicalName());
		}

		T pojoFed = pojoFedOpt.get();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			feedField(pojoFed, field, luceneParentName);
		}

		return pojoFed;
	}

	private <T> void feedField(T pojoToFeed, Field field, String luceneParentName) throws IllegalAccessException,
			InstanceCreationException, InvocationTargetException, InstantiationException {
		Class<?> subClazz = field.getType();
		String luceneName = addSubClazzName(luceneParentName, subClazz);
		switch (getFieldType(subClazz)) {
			case BYTE:
				field.set(pojoToFeed, generateByteValue(luceneName));
				break;
			case NUMERIC:
				field.set(pojoToFeed, generateNumericValue(luceneName));
				break;
			case CHAR:
				field.set(pojoToFeed, generateCharValue(luceneName));
				break;
			case STRING:
				field.set(pojoToFeed, generateStringValue(luceneName));
				break;
			case ARRAY:
				manageArray(pojoToFeed, field, luceneName);
				break;
			case COLLECTION:
				field.set(pojoToFeed, generateValueInCollection(subClazz, luceneName));
				break;
			case MAP:
				field.set(pojoToFeed, generateValuesInMap(subClazz, luceneName));
				break;
			case OBJECT:
				field.set(pojoToFeed, generatePojoFed(subClazz, luceneName));
				break;
			case ANONYME:
			case DATE:
			default:
				log.info("Error checking field type not managed by generator: {}", luceneName);
				break;
		}
	}

	private <T> void manageArray(T pojoToFeed, Field field, String luceneName) throws IllegalAccessException,
			InstantiationException, InstanceCreationException, InvocationTargetException {
		switch (getArrayType(field.getType())){
			case BYTE:
				field.set(pojoToFeed, generateByteArray(luceneName));
				break;
			case NUMERIC:
				field.set(pojoToFeed, generateNumericValue(luceneName));
				break;
			case CHAR:
				field.set(pojoToFeed, generateCharArray(luceneName));
			case STRING:
				field.set(pojoToFeed, generateStringValue(luceneName));
				break;
			case ARRAY:
				manageArray(pojoToFeed, field, luceneName);
				break;
			case COLLECTION:
				field.set(pojoToFeed, generateValueInCollection(field.getType(), luceneName));
				break;
			case MAP:
				field.set(pojoToFeed, generateValuesInMap(field.getType(), luceneName));
				break;
			case OBJECT:
				field.set(pojoToFeed, generatePojoFed(field.getType(), luceneName));
				break;
			case ANONYME:
			case DATE:
			default:
				log.info("Error checking field type not managed by generator: {}", luceneName);
				break;
		}
	}

	private FieldTypeEnum getFieldType(Class<?> clazz) {
		if (Byte.class.isAssignableFrom(clazz)) return FieldTypeEnum.BYTE;
		if (isNumericClass(clazz)) return FieldTypeEnum.NUMERIC;
		if (Character.class.isAssignableFrom(clazz)) return FieldTypeEnum.CHAR;
		if (String.class.isAssignableFrom(clazz)) return FieldTypeEnum.STRING;
		if (clazz.isArray()) return FieldTypeEnum.ARRAY;
		if (isCollectionInstance(clazz)) return FieldTypeEnum.COLLECTION;
		if (isMapInstance(clazz)) return FieldTypeEnum.MAP;
		if (clazz.isAnonymousClass()) return FieldTypeEnum.ANONYME;
		return UNKNOWN;
	}

	private FieldTypeEnum getArrayType(Class<?> clazz) {
		if (Byte[].class.isAssignableFrom(clazz)) return FieldTypeEnum.BYTE;
		if (isNumericArrayClass(clazz)) return FieldTypeEnum.NUMERIC;
		if (Character[].class.isAssignableFrom(clazz)) return FieldTypeEnum.CHAR;
		if (String[].class.isAssignableFrom(clazz)) return FieldTypeEnum.STRING;
		if (clazz.isArray()) return FieldTypeEnum.ARRAY;
		if (isCollectionInstance(clazz)) return FieldTypeEnum.COLLECTION;
		if (isMapInstance(clazz)) return FieldTypeEnum.MAP;
		if (clazz.isAnonymousClass()) return FieldTypeEnum.ANONYME;
		return UNKNOWN;
	}

	private boolean isNumericArrayClass(Class<?> clazz) {
		return Short[].class.isAssignableFrom(clazz)
				|| Integer[].class.isAssignableFrom(clazz)
				|| Long[].class.isAssignableFrom(clazz)
				|| Float[].class.isAssignableFrom(clazz)
				|| Double[].class.isAssignableFrom(clazz);
	}

	private String addSubClazzName(String luceneParentName, Class<?> subClazz) {
		return luceneParentName + "." + subClazz.getName();
	}

	private boolean isMapInstance(Class<?> subClazz) {
		return Map.class.isAssignableFrom(subClazz);
	}

	private boolean isCollectionInstance(Class<?> subClazz) {
		return Collection.class.isAssignableFrom(subClazz);
	}

	private boolean isNumericClass(Class<?> clazz) {
		return Short.class.isAssignableFrom(clazz)
				|| Integer.class.isAssignableFrom(clazz)
				|| Long.class.isAssignableFrom(clazz)
				|| Float.class.isAssignableFrom(clazz)
				|| Double.class.isAssignableFrom(clazz);
	}

	private Collection<?> generateValueInCollection(Class<?> subClazz, String luceneName) {
		return null;
	}

	private Map<?, ?> generateValuesInMap(Class<?> subClazz, String luceneName) {
		return null;
	}

	public <T> Optional<T> newInstance(Class<T> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
		Constructor<?>[] constructors = clazz.getConstructors();
		Constructor<?> firstConstructor = constructors[0];
		// TODO Increase IA level
		T pojoFed = (T) firstConstructor.newInstance(null); // try with first constructor
		return Optional.ofNullable(pojoFed);
	}
}
