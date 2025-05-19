package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.model.Pig;
import org.openapitools.model.Whale;
import org.openapitools.model.Zebra;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "className", visible = true)
@JsonSubTypes({
})

public class Mammal   {
  
  private Boolean hasBaleen;

  private Boolean hasTeeth;

  private String className;


public enum TypeEnum {

    @JsonProperty("plains") PLAINS(String.valueOf("plains")), @JsonProperty("mountain") MOUNTAIN(String.valueOf("mountain")), @JsonProperty("grevys") GREVYS(String.valueOf("grevys"));


    private String value;

    TypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static TypeEnum fromValue(String value) {
        for (TypeEnum b : TypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  private TypeEnum type;

  /**
   **/
  public Mammal hasBaleen(Boolean hasBaleen) {
    this.hasBaleen = hasBaleen;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("hasBaleen")
  public Boolean getHasBaleen() {
    return hasBaleen;
  }
  public void setHasBaleen(Boolean hasBaleen) {
    this.hasBaleen = hasBaleen;
  }


  /**
   **/
  public Mammal hasTeeth(Boolean hasTeeth) {
    this.hasTeeth = hasTeeth;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("hasTeeth")
  public Boolean getHasTeeth() {
    return hasTeeth;
  }
  public void setHasTeeth(Boolean hasTeeth) {
    this.hasTeeth = hasTeeth;
  }


  /**
   **/
  public Mammal className(String className) {
    this.className = className;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("className")
  @NotNull
  public String getClassName() {
    return className;
  }
  public void setClassName(String className) {
    this.className = className;
  }


  /**
   **/
  public Mammal type(TypeEnum type) {
    this.type = type;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }
  public void setType(TypeEnum type) {
    this.type = type;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Mammal mammal = (Mammal) o;
    return Objects.equals(this.hasBaleen, mammal.hasBaleen) &&
        Objects.equals(this.hasTeeth, mammal.hasTeeth) &&
        Objects.equals(this.className, mammal.className) &&
        Objects.equals(this.type, mammal.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hasBaleen, hasTeeth, className, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Mammal {\n");
    
    sb.append("    hasBaleen: ").append(toIndentedString(hasBaleen)).append("\n");
    sb.append("    hasTeeth: ").append(toIndentedString(hasTeeth)).append("\n");
    sb.append("    className: ").append(toIndentedString(className)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

