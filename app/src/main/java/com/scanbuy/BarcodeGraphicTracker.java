package com.scanbuy;

/**
 * Created by kshitiz on 11/7/15.
 */
import com.scanbuy.com.scanabook.camera.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;


class BarcodeGraphicTracker extends Tracker<Barcode> {
    private GraphicOverlay<BarcodeGraphic> mOverlay;
    private BarcodeGraphic mGraphic;

    BarcodeGraphicTracker(GraphicOverlay<BarcodeGraphic> overlay, BarcodeGraphic graphic) {
        mOverlay = overlay;
        mGraphic = graphic;
    }


    @Override
    public void onNewItem(int id, Barcode item) {
        mGraphic.setId(id);
    }


    @Override
    public void onUpdate(Detector.Detections<Barcode> detectionResults, Barcode item) {
        mOverlay.add(mGraphic);
        mGraphic.updateItem(item);
    }


    @Override
    public void onMissing(Detector.Detections<Barcode> detectionResults) {
        mOverlay.remove(mGraphic);
    }


    @Override
    public void onDone() {
        mOverlay.remove(mGraphic);
    }
}