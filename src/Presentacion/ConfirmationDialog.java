package Presentacion;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.util.ResourceBundle;

/** Displays a confirmation dialog to which a yes or no response is expected.
 *  @author http://stackoverflow.com/questions/12717969/javafx-2-custom-popup-pane
 */
public class ConfirmationDialog {
    final private Stage  dialog;
    final private Pane   layout;
    final private Button yesButton;
    final private Button noButton;
    final private Parent ownerRoot;
    final private Delta  dragDelta = new Delta();

    final private ObjectProperty<Effect> ownerEffectProperty = new SimpleObjectProperty<>();
    final private BooleanProperty        isDraggableProperty = new SimpleBooleanProperty();
    final private ObjectProperty<Side>   sideProperty        = new SimpleObjectProperty();
    final private ReadOnlyBooleanWrapper confirmedProperty   = new ReadOnlyBooleanWrapper();
    final private ObjectProperty<EventHandler<ActionEvent>> onYesProperty = new SimpleObjectProperty<>();
    final private ObjectProperty<EventHandler<ActionEvent>> onNoProperty  = new SimpleObjectProperty<>();

    // default action to take on yes chosen.
    final private EventHandler<ActionEvent> YES_HANDLER = new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent event) {
            if (onYesProperty.get() != null) {
                onYesProperty.get().handle(event);
            }
            if (!event.isConsumed()) {
                confirmedProperty.setValue(true);
                if (ownerEffectProperty.get() != null) {
                    ownerRoot.setEffect(null);
                }
                dialog.close();
            }
        }
    };

    // default action to take on no chosen.
    final private EventHandler<ActionEvent> NO_HANDLER = new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent event) {
            if (onNoProperty.get() != null)  {
                onNoProperty.get().handle(event);
            }
            if (!event.isConsumed()) {
                confirmedProperty.setValue(false);
                if (ownerEffectProperty.get() != null) {
                    ownerRoot.setEffect(null);
                }
                dialog.close();
            }
        }
    };

    // perform the drag operation.
    private EventHandler<MouseEvent> ON_MOUSE_DRAGGED_DRAG_HANDLER = new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
            dialog.setX(mouseEvent.getScreenX() + dragDelta.x);
            dialog.setY(mouseEvent.getScreenY() + dragDelta.y);
        }
    };

    // record a delta distance for the drag and drop operation.
    private final EventHandler<MouseEvent> ON_MOUSE_PRESSED_DRAG_HANDLER = new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
            dragDelta.x = dialog.getX() - mouseEvent.getScreenX();
            dragDelta.y = dialog.getY() - mouseEvent.getScreenY();
        }
    };

    // switch the drag option on and off as appropriate.
    private final ChangeListener<Boolean> DRAG_CHANGE_LISTENER = new ChangeListener<Boolean>() {
        @Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (newValue == null || !newValue) {
                makeUndraggable();
            } else {
                makeDraggable();
            }
        }
    };

    // display the dialog at the appropriate side of the owner.
    private final ChangeListener<Side> SIDE_CHANGE_LISTENER = new ChangeListener<Side>() {
        @Override public void changed(ObservableValue<? extends Side> observable, Side oldValue, final Side newValue) {
            if (dialog.isShowing()) {
                relocateDialog(newValue);
            } else {
                dialog.setOnShown(new EventHandler<WindowEvent>() {
                    @Override public void handle(WindowEvent event) {
                        relocateDialog(newValue);
                    }
                });
            }
        }
    };

    /**
     * Create a new confirmation dialog.
     * @param owner the window which owns this dialog (the parent of the dialog).
     * @param question a question the dialog will ask confirmation for (should have a yes or no answer).
     */
    public ConfirmationDialog(Window owner, String question) {

        dialog = new Stage(StageStyle.TRANSPARENT);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(owner);

        dialog.setY(owner.getHeight() + owner.getY());

        yesButton = ButtonBuilder.create().text("Acceptar").onAction(YES_HANDLER).defaultButton(true).build();
        noButton  = ButtonBuilder.create().text("Cancelar").onAction(NO_HANDLER).cancelButton(true).build();
        ownerRoot = owner.getScene().getRoot();

        layout    = HBoxBuilder.create().styleClass("modal-dialog").build();
        layout.getChildren().addAll(
                LabelBuilder.create().text(question).build(),
                yesButton,
                noButton
        );

        dialog.setScene(createScene(layout));

        isDraggableProperty.addListener(DRAG_CHANGE_LISTENER);
        isDraggableProperty.set(true);

        sideProperty.addListener(SIDE_CHANGE_LISTENER);
    }

    /**
     * Show the dialog and pause execution of the calling thread until
     * the user makes a confirmation selection.
     */
    public void showAndWait() {
        if (ownerEffectProperty.get() != null) {
            ownerRoot.setEffect(ownerEffectProperty.get());
        }
        dialog.showAndWait();
    }

    /**
     * Show the dialog and immediately continue execution of the calling thread.
     */
    public void show() {
        if (ownerEffectProperty.get() != null) {
            ownerRoot.setEffect(ownerEffectProperty.get());
        }
        dialog.show();
    }

    private void makeDraggable() {
        layout.getStyleClass().add("draggable");
        layout.setOnMousePressed(ON_MOUSE_PRESSED_DRAG_HANDLER);
        layout.setOnMouseDragged(ON_MOUSE_DRAGGED_DRAG_HANDLER);
    }

    private void makeUndraggable() {
        layout.getStyleClass().remove("draggable");
        layout.setOnMousePressed(null);
        layout.setOnMouseDragged(null);
    }

    private Scene createScene(Parent layout) {
        Scene scene = new Scene(layout, Color.TRANSPARENT);
        //scene.getStylesheets().add(ConfirmationDialog.class.getResource("modal-dialog.css").toExternalForm());
        return scene;
    }

    private void relocateDialog(Side newValue) {
        if (newValue != null) {
            final Window owner = dialog.getOwner();
            switch (newValue) {
                case TOP:
                    setDialogX(owner.getX() + owner.getWidth() / 2 - dialog.getWidth() / 2);
                    setDialogY(owner.getY() - dialog.getHeight());
                    break;

                case RIGHT:
                    setDialogX(owner.getX() + owner.getWidth());
                    setDialogY(owner.getY() + owner.getHeight() / 2 - dialog.getHeight() / 2);
                    break;

                case BOTTOM:
                    setDialogX(owner.getX() + owner.getWidth() / 2 - dialog.getWidth() / 2);
                    setDialogY(owner.getY() + owner.getHeight());
                    break;

                case LEFT:
                    setDialogX(owner.getX() - dialog.getWidth());
                    setDialogY(owner.getY() + owner.getHeight() / 2 - dialog.getHeight() / 2);
                    break;
            }
        } else {
            dialog.centerOnScreen();
        }
    }

    final private static double EDGE_INSET = 100;

    private void setDialogX(double x) {
        final double min = EDGE_INSET;
        final double max = getMaxX() - dialog.getWidth() - EDGE_INSET;
        x = Math.min(max, x);
        x = Math.max(min, x);

        dialog.setX(x);
    }

    private void setDialogY(double y) {
        final double min = EDGE_INSET;
        final double max = getMaxY() - dialog.getHeight() - EDGE_INSET;
        y = Math.min(max, y);
        y = Math.max(min, y);

        dialog.setY(y);
    }

    private double getMaxX() {
        double maxX = 0;
        for (final Screen s: Screen.getScreens()) {
            maxX = Math.max(maxX, s.getVisualBounds().getMaxX());
        }
        return maxX;
    }

    private double getMaxY() {
        double maxY = 0;
        for (final Screen s: Screen.getScreens()) {
            maxY = Math.max(maxY, s.getVisualBounds().getMaxY());
        }
        return maxY;
    }

    /**
     * @return the confirmation selection the user has made.
     */
    public ReadOnlyBooleanProperty confirmedProperty() {
        return confirmedProperty.getReadOnlyProperty();
    }

    /**
     * @return true if the user answered yes to the question,
     *         false if the user answered no to the question,
     *         null if the user has not yet made a selection.
     */
    public Boolean isConfirmed() {
        return confirmedProperty.getValue();
    }

    /** @return custom action which can be taken on yes. */
    public ObjectProperty<EventHandler<ActionEvent>> onYesProperty() {
        return onYesProperty;
    }

    /** @param onYes custom action which can be taken on yes. */
    public void setOnYes(EventHandler<ActionEvent> onYes) {
        onYesProperty.set(onYes);
    }

    /** @return custom action which can be taken on yes. */
    public EventHandler<ActionEvent> getOnYes() {
        return onYesProperty.get();
    }

    /** @return custom action which can be taken on no. */
    public ObjectProperty<EventHandler<ActionEvent>> onNoProperty() {
        return onNoProperty;
    }

    /** @param onNo custom action which can be taken on no. */
    public void setOnNo(EventHandler<ActionEvent> onNo) {
        onNoProperty.set(onNo);
    }

    /** @return custom action which can be taken on no. */
    public EventHandler<ActionEvent> getOnNo() {
        return onNoProperty.get();
    }

    /** @return effect to be applied to the owner stage when the dialog is shown. */
    public ObjectProperty<Effect> ownerEffectProperty() {
        return ownerEffectProperty;
    }

    /** @param ownerEffect effect to be applied to the owner stage when the dialog is shown. */
    public void setOwnerEffect(Effect ownerEffect) {
        ownerEffectProperty.set(ownerEffect);
    }

    /** @param effect to be applied to the owner stage when the dialog is shown. */
    public Effect getOwnerEffect() {
        return ownerEffectProperty.get();
    }

    /** @return whether or not the dialog can be dragged around. */
    public BooleanProperty isDraggableProperty() {
        return isDraggableProperty;
    }

    /** @param isDraggable whether or not the dialog can be dragged around. */
    public void setDraggable(boolean isDraggable) {
        isDraggableProperty.set(isDraggable);
    }

    /** @return whether or not the dialog can be dragged around. */
    public boolean isDraggable() {
        return isDraggableProperty.getValue();
    }

    /** @param side the side of the owner at which the dialog is to be displayed. */
    public void setSide(Side side) {
        sideProperty.set(side);
    }

    /** @return the side of the owner at which the dialog is to be displayed. */
    public void getSide(Side side) {
        sideProperty.get();
    }

    // records relative x and y co-ordinates.
    private class Delta { double x, y; }
}