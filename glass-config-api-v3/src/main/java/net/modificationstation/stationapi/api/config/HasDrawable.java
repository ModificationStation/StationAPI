package net.modificationstation.stationapi.api.config;

public interface HasDrawable {

    /**
     * Use this to render your drawable on the screen.
     */
    void draw(int mouseX, int mouseY);

    /**
     * Note: This is called any time the mouse is clicked. Check the button ID if you want to only do stuff with your button.
     */
    void mouseClicked(int mouseX, int mouseY, int button);

    /**
     * Called when setting the drawable position. X and Y is the top left corner.
     */
    void setXYWH(int x, int y, int width, int height);

    /**
     * Called every frame.
     */
    void tick();

    /**
     * Called whenever a key is pressed.
     */
    void keyPressed(char character, int key);

    /**
     * Called when the config category is adding the drawable.
     */
    void setID(int id);
}
