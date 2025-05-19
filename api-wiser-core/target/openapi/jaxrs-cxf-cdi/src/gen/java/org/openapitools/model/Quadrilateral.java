package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.model.ComplexQuadrilateral;
import org.openapitools.model.SimpleQuadrilateral;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "quadrilateralType", visible = true)
@JsonSubTypes({
})

public class Quadrilateral   {
  
  private String shapeType;

  private String quadrilateralType;

  /**
   **/
  public Quadrilateral shapeType(String shapeType) {
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
  public Quadrilateral quadrilateralType(String quadrilateralType) {
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
    Quadrilateral quadrilateral = (Quadrilateral) o;
    return Objects.equals(this.shapeType, quadrilateral.shapeType) &&
        Objects.equals(this.quadrilateralType, quadrilateral.quadrilateralType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shapeType, quadrilateralType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Quadrilateral {\n");
    
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

