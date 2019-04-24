//package SlimeMouldApp;
//import java.util.ArrayList;
//
//import SlimeMouldApp.Element;
//import javafx.animation.AnimationTimer;
//import javafx.application.Application;
//import javafx.scene.Group;
//import javafx.scene.Scene;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.stage.Stage;
//
//public class Simulator extends Application {
//    ArrayList<Element> elements;
////    TrafficLight[] trafficLights;
////    TrafficLight northTrafficLight, westTrafficLight, eastTrafficLight, southTrafficLight;
//
//    Group root;
//    Canvas canvas;
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Simulator");
//
//        root = new Group();
//        cars = new ArrayList<Element>();
//        cars.add(new Car());
//        canvas = new Canvas(800, 800);
//        trafficLights = new TrafficLight[4];
//
//        // TrafficLights
//        trafficLights[0] = new TrafficLight(185, 135, 100, 200, true, "N");
//        trafficLights[1] = new TrafficLight(135, 565, 200, 100, true, "W");
//        trafficLights[2] = new TrafficLight(565, 185, 200, 100, false, "E");
//        trafficLights[3] = new TrafficLight(565, 565, 100, 200, false, "S");
//
//        GraphicsContext gc = canvas.getGraphicsContext2D();
//        drawShapes(gc);
//        update();
//        root.getChildren().add(canvas);
//
//        Scene scene = new Scene(root, 800, 800);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    private void drawShapes(GraphicsContext gc) {
//
//        // Draw Intersection
//        gc.setLineWidth(10.0);
//
//        gc.strokeLine(250, 0, 250, 250);
//        gc.strokeLine(0, 250, 250, 250);
//
//        gc.strokeLine(550, 0, 550, 250);
//        gc.strokeLine(550, 250, 800, 250);
//
//        gc.strokeLine(250, 550, 250, 800);
//        gc.strokeLine(250, 550, 0, 550);
//
//        gc.strokeLine(550, 550, 550, 800);
//        gc.strokeLine(550, 550, 800, 550);
//
//        // Draw Cars
//        for (int i = 0; i < cars.size(); i++) {
//            root.getChildren().add(cars.get(i).drawCar());
//        }
//
//        // Draw Traffic light
//        for (int i = 0; i < trafficLights.length; i++) {
//            root.getChildren().add(trafficLights[i].drawTL());
//            root.getChildren().add(trafficLights[i].drawRedCircle());
//            root.getChildren().add(trafficLights[i].drawGreenCircle());
//        }
//
//    }
//
//    private void update() {
//
//        AnimationTimer timer = new AnimationTimer() {
//            @Override
//            public void handle(long arg0) {
//                // TODO Auto-generated method stub
//                for (int i = 0; i < cars.size(); i++) {
//                    String side = cars.get(i).getSide();
//                    if (side == "N") {
//                        if (cars.get(i).getYPosition() > 224) {
//                            cars.get(i).stopCar();
//                        } else {
//                            // car = new Car(trafficLights[0]);
//                            cars.get(i).incrementYPosition();
//                            cars.get(i).drawCar();
//                        }
//                    } else if (side == "S") {
//                        if (cars.get(i).getYPosition() < 550) {
//                            cars.get(i).stopCar();
//                        } else {
//                            // car = new Car(trafficLights[0]);
//                            cars.get(i).decrementYPosition();
//                            cars.get(i).drawCar();
//                            // System.out.println(car.getYPosition());
//                        }
//                    } else if (side == "W") {
//                        if (cars.get(i).getXPosition() > 224) {
//                            cars.get(i).stopCar();
//                        } else {
//                            // car = new Car(trafficLights[0]);
//                            cars.get(i).incrementXPosition();
//                            cars.get(i).drawCar();
//                        }
//                    } else {
//                        if (cars.get(i).getXPosition() < 555) {
//                            cars.get(i).stopCar();
//                        } else {
//                            // car = new Car(trafficLights[0]);
//                            cars.get(i).decrementXPosition();
//                            cars.get(i).drawCar();
//                            // System.out.println(car.getXPosition());
//                        }
//                    }
//                }
//                System.out.println(cars.size());
//            }
//        };
//        timer.start();
//    }
//}