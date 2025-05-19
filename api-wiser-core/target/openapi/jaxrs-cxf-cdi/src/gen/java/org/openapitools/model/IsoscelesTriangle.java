package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;



public class IsoscelesTriangle   {
  
  private String shapeType;

  private String triangleType;

  /**
   **/
  public IsoscelesTriangle shapeType(String shapeType) {
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
  public IsoscelesTriangle triangleType(String triangleType) {
    this.triangleType = triangleType;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("triangleType")
  @NotNull
  public String getTriangleType() {
    return triangleType;
  }
  public void setTriangleType(String triangleType) {
    this.triangleType = triangleType;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IsoscelesTriangle isoscelesTriangle = (IsoscelesTriangle) o;
    return Objects.equals(this.shapeType, isoscelesTriangle.shapeType) &&
        Objects.equals(this.triangleType, isoscelesTriangle.triangleType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shapeType, triangleType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IsoscelesTriangle {\n");
    
    sb.append("    shapeType: ").append(toIndentedString(shapeType)).append("\n");
    sb.append("    triangleType: ").append(toIndentedString(triangleType)).append("\n");
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

