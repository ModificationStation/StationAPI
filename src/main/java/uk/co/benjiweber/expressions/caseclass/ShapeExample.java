package uk.co.benjiweber.expressions.caseclass;

import static uk.co.benjiweber.expressions.caseclass.ShapeExample.Rectangle.rectangle;

public class ShapeExample {

    interface Shape extends Case3<Circle, Square, Rectangle> {}

    interface Circle extends Shape {
        int radius();
        static Circle circle(int radius) {
            return () -> radius;
        }
    }

    interface Square extends Shape {
        int width();
        static Square square(int width) {
            return () -> width;
        }
    }

    interface Rectangle extends Shape {
        int width();
        int height();
        static Rectangle rectangle(int width, int height) {
            return new Rectangle() {
                public int width() { return width; }
                public int height() { return height; }
            };
        }
    }

    public static void main(String... args) {
        System.out.println(description(rectangle(5, 4)));
    }

    private static String description(Shape shape) {
        return shape.match()
            .when(Circle.class, circle -> "circle" + circle.radius())
            .when(Square.class, square -> "square" + square.width())
            .when(Rectangle.class, rectangle -> "rectangle" + rectangle.width() + rectangle.height());
    }

}
