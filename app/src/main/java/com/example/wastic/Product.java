package com.example.wastic;

public class Product {


        private int id;
        private String name, barcode, photoURL;

        public Product(int id, String name, String barcode, String photoURL) {
            this.id = id;
            this.name = name;
            this.barcode = barcode;
            this.photoURL = photoURL;
        }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getPhotoURL() {
        return photoURL;
    }
}
