package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;



public class ComplexQuadrilateral   {
  
  private String shapeType;

  private String quadrilateralType;

  /**
   **/
  public ComplexQuadrilateral shapeType(String shapeType) {
    this.shapeType = shapeType;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("shapeType")
  @NotNull
  public String getShapeType() {
    return shapeType;
  }
  public void setShapeType(String shapeType) {
    this.shapeType = shapeType;
  }


  /**
   **/
  public ComplexQuadrilateral quadrilateralType(String quadrilateralType) {
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
    ComplexQuadrilateral complexQuadrilateral = (ComplexQuadrilateral) o;
    return Objects.equals(this.shapeType, complexQuadrilateral.shapeType) &&
        Objects.equals(this.quadrilateralType, complexQuadrilateral.quadrilateralType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shapeType, quadrilateralType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComplexQuadrilateral {\n");
    
    sb.append("    shapeType: ").append(toIndentedString(shapeType)).append("\n");
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

