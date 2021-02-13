package uk.co.benjiweber.expressions.caseclass;

import static uk.co.benjiweber.expressions.caseclass.ShapeExample.Rectangle.rectangle;

import uk.co.benjiweber.expressions.caseclass.ShapeExample.Circle;
import uk.co.benjiweber.expressions.caseclass.ShapeExample.Square;

public class ShapeExample {

    public static void main(String... args) {
        System.out.println(description(rectangle(5, 4)));
    }

    private static String description(Shape shape) {
        return shape.match()
                .when(Circle.class, circle -> "circle" + circle.radius())
                .when(Square.class, square -> "square" + square.width())
                .when(Rectangle.class, rectangle -> "rectangle" + rectangle.width() + rectangle.height());
    }

    interface Shape extends Case3<Circle, Square, Rectangle> {
    }

    interface Circle extends Shape {
        static Circle circle(int radius) {
            return () -> radius;
        }

        int radius();
    }

    interface Square extends Shape {
        static Square square(int width) {
            return () -> width;
        }

        int width();
    }

    interface Rectangle extends Shape {
        static Rectangle rectangle(int width, int height) {
            return new Rectangle() {
                @Override
                public int width() {
                    return width;
                }

                @Override
                public int height() {
                    return height;
                }
            };
        }

        int width();

        int height();
    }

}
