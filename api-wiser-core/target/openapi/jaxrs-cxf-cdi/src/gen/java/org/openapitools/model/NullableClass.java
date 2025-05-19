package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.LocalDate;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;



public class NullableClass extends HashMap<String, Object>  {
  
  private Integer integerProp;

  private BigDecimal numberProp;

  private Boolean booleanProp;

  private String stringProp;

  private LocalDate dateProp;

  private java.util.Date datetimeProp;

  private List<Object> arrayNullableProp;

  private List<Object> arrayAndItemsNullableProp;

  private List<Object> arrayItemsNullable = new ArrayList<>();

  private Map<String, Object> objectNullableProp;

  private Map<String, Object> objectAndItemsNullableProp;

  private Map<String, Object> objectItemsNullable = new HashMap<>();

  /**
   **/
  public NullableClass integerProp(Integer integerProp) {
    this.integerProp = integerProp;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("integer_prop")
  public Integer getIntegerProp() {
    return integerProp;
  }
  public void setIntegerProp(Integer integerProp) {
    this.integerProp = integerProp;
  }


  /**
   **/
  public NullableClass numberProp(BigDecimal numberProp) {
    this.numberProp = numberProp;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("number_prop")
  public BigDecimal getNumberProp() {
    return numberProp;
  }
  public void setNumberProp(BigDecimal numberProp) {
    this.numberProp = numberProp;
  }


  /**
   **/
  public NullableClass booleanProp(Boolean booleanProp) {
    this.booleanProp = booleanProp;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("boolean_prop")
  public Boolean getBooleanProp() {
    return booleanProp;
  }
  public void setBooleanProp(Boolean booleanProp) {
    this.booleanProp = booleanProp;
  }


  /**
   **/
  public NullableClass stringProp(String stringProp) {
    this.stringProp = stringProp;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("string_prop")
  public String getStringProp() {
    return stringProp;
  }
  public void setStringProp(String stringProp) {
    this.stringProp = stringProp;
  }


  /**
   **/
  public NullableClass dateProp(LocalDate dateProp) {
    this.dateProp = dateProp;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("date_prop")
  public LocalDate getDateProp() {
    return dateProp;
  }
  public void setDateProp(LocalDate dateProp) {
    this.dateProp = dateProp;
  }


  /**
   **/
  public NullableClass datetimeProp(java.util.Date datetimeProp) {
    this.datetimeProp = datetimeProp;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("datetime_prop")
  public java.util.Date getDatetimeProp() {
    return datetimeProp;
  }
  public void setDatetimeProp(java.util.Date datetimeProp) {
    this.datetimeProp = datetimeProp;
  }


  /**
   **/
  public NullableClass arrayNullableProp(List<Object> arrayNullableProp) {
    this.arrayNullableProp = arrayNullableProp;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("array_nullable_prop")
  public List<Object> getArrayNullableProp() {
    return arrayNullableProp;
  }
  public void setArrayNullableProp(List<Object> arrayNullableProp) {
    this.arrayNullableProp = arrayNullableProp;
  }

  public NullableClass addArrayNullablePropItem(Object arrayNullablePropItem) {
    if (this.arrayNullableProp == null) {
      this.arrayNullableProp = new ArrayList<>();
    }
    this.arrayNullableProp.add(arrayNullablePropItem);
    return this;
  }


  /**
   **/
  public NullableClass arrayAndItemsNullableProp(List<Object> arrayAndItemsNullableProp) {
    this.arrayAndItemsNullableProp = arrayAndItemsNullableProp;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("array_and_items_nullable_prop")
  public List<Object> getArrayAndItemsNullableProp() {
    return arrayAndItemsNullableProp;
  }
  public void setArrayAndItemsNullableProp(List<Object> arrayAndItemsNullableProp) {
    this.arrayAndItemsNullableProp = arrayAndItemsNullableProp;
  }

  public NullableClass addArrayAndItemsNullablePropItem(Object arrayAndItemsNullablePropItem) {
    if (this.arrayAndItemsNullableProp == null) {
      this.arrayAndItemsNullableProp = new ArrayList<>();
    }
    this.arrayAndItemsNullableProp.add(arrayAndItemsNullablePropItem);
    return this;
  }


  /**
   **/
  public NullableClass arrayItemsNullable(List<Object> arrayItemsNullable) {
    this.arrayItemsNullable = arrayItemsNullable;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("array_items_nullable")
  public List<Object> getArrayItemsNullable() {
    return arrayItemsNullable;
  }
  public void setArrayItemsNullable(List<Object> arrayItemsNullable) {
    this.arrayItemsNullable = arrayItemsNullable;
  }

  public NullableClass addArrayItemsNullableItem(Object arrayItemsNullableItem) {
    if (this.arrayItemsNullable == null) {
      this.arrayItemsNullable = new ArrayList<>();
    }
    this.arrayItemsNullable.add(arrayItemsNullableItem);
    return this;
  }


  /**
   **/
  public NullableClass objectNullableProp(Map<String, Object> objectNullableProp) {
    this.objectNullableProp = objectNullableProp;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("object_nullable_prop")
  public Map<String, Object> getObjectNullableProp() {
    return objectNullableProp;
  }
  public void setObjectNullableProp(Map<String, Object> objectNullableProp) {
    this.objectNullableProp = objectNullableProp;
  }


  public NullableClass putObjectNullablePropItem(String key, Object objectNullablePropItem) {
    if (this.objectNullableProp == null) {
      this.objectNullableProp = new HashMap<>();
    }
    this.objectNullableProp.put(key, objectNullablePropItem);
    return this;
  }

  /**
   **/
  public NullableClass objectAndItemsNullableProp(Map<String, Object> objectAndItemsNullableProp) {
    this.objectAndItemsNullableProp = objectAndItemsNullableProp;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("object_and_items_nullable_prop")
  public Map<String, Object> getObjectAndItemsNullableProp() {
    return objectAndItemsNullableProp;
  }
  public void setObjectAndItemsNullableProp(Map<String, Object> objectAndItemsNullableProp) {
    this.objectAndItemsNullableProp = objectAndItemsNullableProp;
  }


  public NullableClass putObjectAndItemsNullablePropItem(String key, Object objectAndItemsNullablePropItem) {
    if (this.objectAndItemsNullableProp == null) {
      this.objectAndItemsNullableProp = new HashMap<>();
    }
    this.objectAndItemsNullableProp.put(key, objectAndItemsNullablePropItem);
    return this;
  }

  /**
   **/
  public NullableClass objectItemsNullable(Map<String, Object> objectItemsNullable) {
    this.objectItemsNullable = objectItemsNullable;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("object_items_nullable")
  public Map<String, Object> getObjectItemsNullable() {
    return objectItemsNullable;
  }
  public void setObjectItemsNullable(Map<String, Object> objectItemsNullable) {
    this.objectItemsNullable = objectItemsNullable;
  }


  public NullableClass putObjectItemsNullableItem(String key, Object objectItemsNullableItem) {
    if (this.objectItemsNullable == null) {
      this.objectItemsNullable = new HashMap<>();
    }
    this.objectItemsNullable.put(key, objectItemsNullableItem);
    return this;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NullableClass nullableClass = (NullableClass) o;
    return super.equals(o) && Objects.equals(this.integerProp, nullableClass.integerProp) &&
        Objects.equals(this.numberProp, nullableClass.numberProp) &&
        Objects.equals(this.booleanProp, nullableClass.booleanProp) &&
        Objects.equals(this.stringProp, nullableClass.stringProp) &&
        Objects.equals(this.dateProp, nullableClass.dateProp) &&
        Objects.equals(this.datetimeProp, nullableClass.datetimeProp) &&
        Objects.equals(this.arrayNullableProp, nullableClass.arrayNullableProp) &&
        Objects.equals(this.arrayAndItemsNullableProp, nullableClass.arrayAndItemsNullableProp) &&
        Objects.equals(this.arrayItemsNullable, nullableClass.arrayItemsNullable) &&
        Objects.equals(this.objectNullableProp, nullableClass.objectNullableProp) &&
        Objects.equals(this.objectAndItemsNullableProp, nullableClass.objectAndItemsNullableProp) &&
        Objects.equals(this.objectItemsNullable, nullableClass.objectItemsNullable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), integerProp, super.hashCode(), numberProp, super.hashCode(), booleanProp, super.hashCode(), stringProp, super.hashCode(), dateProp, super.hashCode(), datetimeProp, super.hashCode(), arrayNullableProp, super.hashCode(), arrayAndItemsNullableProp, super.hashCode(), arrayItemsNullable, super.hashCode(), objectNullableProp, super.hashCode(), objectAndItemsNullableProp, super.hashCode(), objectItemsNullable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NullableClass {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    integerProp: ").append(toIndentedString(integerProp)).append("\n");
    sb.append("    numberProp: ").append(toIndentedString(numberProp)).append("\n");
    sb.append("    booleanProp: ").append(toIndentedString(booleanProp)).append("\n");
    sb.append("    stringProp: ").append(toIndentedString(stringProp)).append("\n");
    sb.append("    dateProp: ").append(toIndentedString(dateProp)).append("\n");
    sb.append("    datetimeProp: ").append(toIndentedString(datetimeProp)).append("\n");
    sb.append("    arrayNullableProp: ").append(toIndentedString(arrayNullableProp)).append("\n");
    sb.append("    arrayAndItemsNullableProp: ").append(toIndentedString(arrayAndItemsNullableProp)).append("\n");
    sb.append("    arrayItemsNullable: ").append(toIndentedString(arrayItemsNullable)).append("\n");
    sb.append("    objectNullableProp: ").append(toIndentedString(objectNullableProp)).append("\n");
    sb.append("    objectAndItemsNullableProp: ").append(toIndentedString(objectAndItemsNullableProp)).append("\n");
    sb.append("    objectItemsNullable: ").append(toIndentedString(objectItemsNullable)).append("\n");
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

