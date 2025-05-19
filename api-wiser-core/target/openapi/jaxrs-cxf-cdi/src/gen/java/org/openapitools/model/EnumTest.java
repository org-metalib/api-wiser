package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.model.OuterEnum;
import org.openapitools.model.OuterEnumDefaultValue;
import org.openapitools.model.OuterEnumInteger;
import org.openapitools.model.OuterEnumIntegerDefaultValue;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;



public class EnumTest   {
  

public enum EnumStringEnum {

    @JsonProperty("UPPER") UPPER(String.valueOf("UPPER")), @JsonProperty("lower") LOWER(String.valueOf("lower")), @JsonProperty("") EMPTY(String.valueOf(""));


    private String value;

    EnumStringEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static EnumStringEnum fromValue(String value) {
        for (EnumStringEnum b : EnumStringEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  private EnumStringEnum enumString;


public enum EnumStringRequiredEnum {

    @JsonProperty("UPPER") UPPER(String.valueOf("UPPER")), @JsonProperty("lower") LOWER(String.valueOf("lower")), @JsonProperty("") EMPTY(String.valueOf(""));


    private String value;

    EnumStringRequiredEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static EnumStringRequiredEnum fromValue(String value) {
        for (EnumStringRequiredEnum b : EnumStringRequiredEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  private EnumStringRequiredEnum enumStringRequired;


public enum EnumIntegerEnum {

    @JsonProperty("1") NUMBER_1(Integer.valueOf(1)), @JsonProperty("-1") NUMBER_MINUS_1(Integer.valueOf(-1));


    private Integer value;

    EnumIntegerEnum(Integer v) {
        value = v;
    }

    public Integer value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static EnumIntegerEnum fromValue(Integer value) {
        for (EnumIntegerEnum b : EnumIntegerEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  private EnumIntegerEnum enumInteger;


public enum EnumIntegerOnlyEnum {

    @JsonProperty("2") NUMBER_2(Integer.valueOf(2)), @JsonProperty("-2") NUMBER_MINUS_2(Integer.valueOf(-2));


    private Integer value;

    EnumIntegerOnlyEnum(Integer v) {
        value = v;
    }

    public Integer value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static EnumIntegerOnlyEnum fromValue(Integer value) {
        for (EnumIntegerOnlyEnum b : EnumIntegerOnlyEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  private EnumIntegerOnlyEnum enumIntegerOnly;


public enum EnumNumberEnum {

    @JsonProperty("1.1") NUMBER_1_DOT_1(Double.valueOf(1.1)), @JsonProperty("-1.2") NUMBER_MINUS_1_DOT_2(Double.valueOf(-1.2));


    private Double value;

    EnumNumberEnum(Double v) {
        value = v;
    }

    public Double value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static EnumNumberEnum fromValue(Double value) {
        for (EnumNumberEnum b : EnumNumberEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  private EnumNumberEnum enumNumber;

  private OuterEnum outerEnum;

  private OuterEnumInteger outerEnumInteger;

  private OuterEnumDefaultValue outerEnumDefaultValue = OuterEnumDefaultValue.PLACED;

  private OuterEnumIntegerDefaultValue outerEnumIntegerDefaultValue = OuterEnumIntegerDefaultValue.NUMBER_0;

  /**
   **/
  public EnumTest enumString(EnumStringEnum enumString) {
    this.enumString = enumString;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("enum_string")
  public EnumStringEnum getEnumString() {
    return enumString;
  }
  public void setEnumString(EnumStringEnum enumString) {
    this.enumString = enumString;
  }


  /**
   **/
  public EnumTest enumStringRequired(EnumStringRequiredEnum enumStringRequired) {
    this.enumStringRequired = enumStringRequired;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("enum_string_required")
  @NotNull
  public EnumStringRequiredEnum getEnumStringRequired() {
    return enumStringRequired;
  }
  public void setEnumStringRequired(EnumStringRequiredEnum enumStringRequired) {
    this.enumStringRequired = enumStringRequired;
  }


  /**
   **/
  public EnumTest enumInteger(EnumIntegerEnum enumInteger) {
    this.enumInteger = enumInteger;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("enum_integer")
  public EnumIntegerEnum getEnumInteger() {
    return enumInteger;
  }
  public void setEnumInteger(EnumIntegerEnum enumInteger) {
    this.enumInteger = enumInteger;
  }


  /**
   **/
  public EnumTest enumIntegerOnly(EnumIntegerOnlyEnum enumIntegerOnly) {
    this.enumIntegerOnly = enumIntegerOnly;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("enum_integer_only")
  public EnumIntegerOnlyEnum getEnumIntegerOnly() {
    return enumIntegerOnly;
  }
  public void setEnumIntegerOnly(EnumIntegerOnlyEnum enumIntegerOnly) {
    this.enumIntegerOnly = enumIntegerOnly;
  }


  /**
   **/
  public EnumTest enumNumber(EnumNumberEnum enumNumber) {
    this.enumNumber = enumNumber;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("enum_number")
  public EnumNumberEnum getEnumNumber() {
    return enumNumber;
  }
  public void setEnumNumber(EnumNumberEnum enumNumber) {
    this.enumNumber = enumNumber;
  }


  /**
   **/
  public EnumTest outerEnum(OuterEnum outerEnum) {
    this.outerEnum = outerEnum;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("outerEnum")
  public OuterEnum getOuterEnum() {
    return outerEnum;
  }
  public void setOuterEnum(OuterEnum outerEnum) {
    this.outerEnum = outerEnum;
  }


  /**
   **/
  public EnumTest outerEnumInteger(OuterEnumInteger outerEnumInteger) {
    this.outerEnumInteger = outerEnumInteger;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("outerEnumInteger")
  public OuterEnumInteger getOuterEnumInteger() {
    return outerEnumInteger;
  }
  public void setOuterEnumInteger(OuterEnumInteger outerEnumInteger) {
    this.outerEnumInteger = outerEnumInteger;
  }


  /**
   **/
  public EnumTest outerEnumDefaultValue(OuterEnumDefaultValue outerEnumDefaultValue) {
    this.outerEnumDefaultValue = outerEnumDefaultValue;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("outerEnumDefaultValue")
  public OuterEnumDefaultValue getOuterEnumDefaultValue() {
    return outerEnumDefaultValue;
  }
  public void setOuterEnumDefaultValue(OuterEnumDefaultValue outerEnumDefaultValue) {
    this.outerEnumDefaultValue = outerEnumDefaultValue;
  }


  /**
   **/
  public EnumTest outerEnumIntegerDefaultValue(OuterEnumIntegerDefaultValue outerEnumIntegerDefaultValue) {
    this.outerEnumIntegerDefaultValue = outerEnumIntegerDefaultValue;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("outerEnumIntegerDefaultValue")
  public OuterEnumIntegerDefaultValue getOuterEnumIntegerDefaultValue() {
    return outerEnumIntegerDefaultValue;
  }
  public void setOuterEnumIntegerDefaultValue(OuterEnumIntegerDefaultValue outerEnumIntegerDefaultValue) {
    this.outerEnumIntegerDefaultValue = outerEnumIntegerDefaultValue;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnumTest enumTest = (EnumTest) o;
    return Objects.equals(this.enumString, enumTest.enumString) &&
        Objects.equals(this.enumStringRequired, enumTest.enumStringRequired) &&
        Objects.equals(this.enumInteger, enumTest.enumInteger) &&
        Objects.equals(this.enumIntegerOnly, enumTest.enumIntegerOnly) &&
        Objects.equals(this.enumNumber, enumTest.enumNumber) &&
        Objects.equals(this.outerEnum, enumTest.outerEnum) &&
        Objects.equals(this.outerEnumInteger, enumTest.outerEnumInteger) &&
        Objects.equals(this.outerEnumDefaultValue, enumTest.outerEnumDefaultValue) &&
        Objects.equals(this.outerEnumIntegerDefaultValue, enumTest.outerEnumIntegerDefaultValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enumString, enumStringRequired, enumInteger, enumIntegerOnly, enumNumber, outerEnum, outerEnumInteger, outerEnumDefaultValue, outerEnumIntegerDefaultValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnumTest {\n");
    
    sb.append("    enumString: ").append(toIndentedString(enumString)).append("\n");
    sb.append("    enumStringRequired: ").append(toIndentedString(enumStringRequired)).append("\n");
    sb.append("    enumInteger: ").append(toIndentedString(enumInteger)).append("\n");
    sb.append("    enumIntegerOnly: ").append(toIndentedString(enumIntegerOnly)).append("\n");
    sb.append("    enumNumber: ").append(toIndentedString(enumNumber)).append("\n");
    sb.append("    outerEnum: ").append(toIndentedString(outerEnum)).append("\n");
    sb.append("    outerEnumInteger: ").append(toIndentedString(outerEnumInteger)).append("\n");
    sb.append("    outerEnumDefaultValue: ").append(toIndentedString(outerEnumDefaultValue)).append("\n");
    sb.append("    outerEnumIntegerDefaultValue: ").append(toIndentedString(outerEnumIntegerDefaultValue)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

