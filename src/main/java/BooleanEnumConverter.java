import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class BooleanEnumConverter implements AttributeConverter<Boolean, String>{

	public String convertToDatabaseColumn(Boolean attribute) {
		// TODO Auto-generated method stub
		return (attribute) ? "S" : "N"; //if true:"S", otherwise "N"
	}

	public Boolean convertToEntityAttribute(String dbData) {
		// TODO Auto-generated method stub

		return dbData.matches("S") ? true : false; //if "S":true, otherwise false		
	}

}
