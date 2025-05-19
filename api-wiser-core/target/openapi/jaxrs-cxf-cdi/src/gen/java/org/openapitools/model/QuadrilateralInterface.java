package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;



public class QuadrilateralInterface   {
  
  private String quadrilateralType;

  /**
   **/
  public QuadrilateralInterface quadrilateralType(String quadrilateralType) {
    this.quadrilateralType = quadrilateralType;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("quadrilateralType")
  @NotNull
  public String getQuadrilateralType() {
    return quadrilateralType;
  }
  public void setQuadrilateralType(String quadrilateralType) {
    this.quadrilateralType = quadrilateralType;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuadrilateralInterface quadrilateralInterface = (QuadrilateralInterface) o;
    return Objects.equals(this.quadrilateralType, quadrilateralInterface.quadrilateralType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quadrilateralType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuadrilateralInterface {\n");
    
    sb.append("    quadrilateralType: ").append(toIndentedString(quadrilateralType)).append("\n");
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

