package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.model.Fruit;
import org.openapitools.model.NullableShape;
import org.openapitools.model.Shape;
import org.openapitools.model.ShapeOrNull;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;



public class Drawing extends HashMap<String, Fruit>  {
  
  private Shape mainShape;

  private ShapeOrNull shapeOrNull;

  private NullableShape nullableShape;

  private List<Shape> shapes = new ArrayList<>();

  /**
   **/
  public Drawing mainShape(Shape mainShape) {
    this.mainShape = mainShape;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("mainShape")
  public Shape getMainShape() {
    return mainShape;
  }
  public void setMainShape(Shape mainShape) {
    this.mainShape = mainShape;
  }


  /**
   **/
  public Drawing shapeOrNull(ShapeOrNull shapeOrNull) {
    this.shapeOrNull = shapeOrNull;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("shapeOrNull")
  public ShapeOrNull getShapeOrNull() {
    return shapeOrNull;
  }
  public void setShapeOrNull(ShapeOrNull shapeOrNull) {
    this.shapeOrNull = shapeOrNull;
  }


  /**
   **/
  public Drawing nullableShape(NullableShape nullableShape) {
    this.nullableShape = nullableShape;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("nullableShape")
  public NullableShape getNullableShape() {
    return nullableShape;
  }
  public void setNullableShape(NullableShape nullableShape) {
    this.nullableShape = nullableShape;
  }


  /**
   **/
  public Drawing shapes(List<Shape> shapes) {
    this.shapes = shapes;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("shapes")
  public List<Shape> getShapes() {
    return shapes;
  }
  public void setShapes(List<Shape> shapes) {
    this.shapes = shapes;
  }

  public Drawing addShapesItem(Shape shapesItem) {
    if (this.shapes == null) {
      this.shapes = new ArrayList<>();
    }
    this.shapes.add(shapesItem);
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
    Drawing drawing = (Drawing) o;
    return super.equals(o) && Objects.equals(this.mainShape, drawing.mainShape) &&
        Objects.equals(this.shapeOrNull, drawing.shapeOrNull) &&
        Objects.equals(this.nullableShape, drawing.nullableShape) &&
        Objects.equals(this.shapes, drawing.shapes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), mainShape, super.hashCode(), shapeOrNull, super.hashCode(), nullableShape, super.hashCode(), shapes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Drawing {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    mainShape: ").append(toIndentedString(mainShape)).append("\n");
    sb.append("    shapeOrNull: ").append(toIndentedString(shapeOrNull)).append("\n");
    sb.append("    nullableShape: ").append(toIndentedString(nullableShape)).append("\n");
    sb.append("    shapes: ").append(toIndentedString(shapes)).append("\n");
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

