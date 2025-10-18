public class Car {
    private Long id;
    private String brand;
    private String model;
    private double pricePerDAy;

    public Car(Long id, String brand, String model, double pricePerDAy){
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.pricePerDAy = pricePerDAy;
    }

    public Long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getPricePerDAy() {
        return pricePerDAy;
    }
