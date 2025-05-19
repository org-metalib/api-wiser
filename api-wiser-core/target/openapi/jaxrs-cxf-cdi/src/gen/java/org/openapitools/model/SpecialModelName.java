package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;



public class SpecialModelName   {
  
  private Long $specialPropertyName;

  private String specialModelName;

  /**
   **/
  public SpecialModelName $specialPropertyName(Long $specialPropertyName) {
    this.$specialPropertyName = $specialPropertyName;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("$special[property.name]")
  public Long get$SpecialPropertyName() {
    return $specialPropertyName;
  }
  public void set$SpecialPropertyName(Long $specialPropertyName) {
    this.$specialPropertyName = $specialPropertyName;
  }


  /**
   **/
  public SpecialModelName specialModelName(String specialModelName) {
    this.specialModelName = specialModelName;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("_special_model.name_")
  public String getSpecialModelName() {
    return specialModelName;
  }
  public void setSpecialModelName(String specialModelName) {
    this.specialModelName = specialModelName;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpecialModelName specialModelName = (SpecialModelName) o;
    return Objects.equals(this.$specialPropertyName, specialModelName.$specialPropertyName) &&
        Objects.equals(this.specialModelName, specialModelName.specialModelName);
  }

  @Override
  public int hashCode() {
    return Objects.hash($specialPropertyName, specialModelName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpecialModelName {\n");
    
    sb.append("    $specialPropertyName: ").append(toIndentedString($specialPropertyName)).append("\n");
    sb.append("    specialModelName: ").append(toIndentedString(specialModelName)).append("\n");
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

