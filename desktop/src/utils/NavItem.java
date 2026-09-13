package utils;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;

public class NavItem {
    private final FontAwesomeIcon icon;
    private final String text;
    public boolean isActive;

    public NavItem(FontAwesomeIcons i, String text, boolean isActive) {
        this.icon = new FontAwesomeIcon();
        icon.setIcon(i);
        this.text = text;
        this.isActive = isActive;
    }

    public FontAwesomeIcon getIcon() {
        return this.icon;
    }

    public String getText() {
        return this.text;
    }

    public String toString() {
        return this.text;
    }
}