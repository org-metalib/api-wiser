package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.model.ParentPet;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;



public class ChildCat extends ParentPet  {
  
  private String name;


public enum CatTypeEnum {

    @JsonProperty("ChildCat") CHILD_CAT(String.valueOf("ChildCat"));


    private String value;

    CatTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static CatTypeEnum fromValue(String value) {
        for (CatTypeEnum b : CatTypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  private CatTypeEnum catType = CatTypeEnum.CHILD_CAT;

  /**
   **/
  public ChildCat name(String name) {
    this.name = name;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }


  /**
   **/
  public ChildCat catType(CatTypeEnum catType) {
    this.catType = catType;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("cat_type")
  public CatTypeEnum getCatType() {
    return catType;
  }
  public void setCatType(CatTypeEnum catType) {
    this.catType = catType;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChildCat childCat = (ChildCat) o;
    return super.equals(o) && Objects.equals(this.name, childCat.name) &&
        Objects.equals(this.catType, childCat.catType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name, super.hashCode(), catType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChildCat {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    catType: ").append(toIndentedString(catType)).append("\n");
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

