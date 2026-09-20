package utils;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;

import java.util.Objects;

public class NavItem {
    private final FontAwesomeIcon icon;
    private final String text;

    private boolean active;

    public NavItem(FontAwesomeIcons i, String text, boolean isActive) {
        this.icon = new FontAwesomeIcon();
        this.icon.setIcon(i);

        this.text = text;
        this.active = isActive;
    }

    public FontAwesomeIcon getIcon() {
        return this.icon;
    }

    public String getText() {
        return this.text;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return this.text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof NavItem)) {
            return false;
        }

        NavItem n = (NavItem) o;
        return Objects.equals(text, n.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}