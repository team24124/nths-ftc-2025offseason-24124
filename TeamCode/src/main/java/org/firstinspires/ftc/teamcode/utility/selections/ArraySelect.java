package org.firstinspires.ftc.teamcode.utility.selections;

/**
 * Non-looping list of objects with moveable index. When the index reaches the end of the list, it resets to the beginning
 *
 * @param <T> Type of object inside the carousel.
 */
public class ArraySelect<T> implements Selector<T> {
    /*
        CarouselSelect representation for array of size four
        -----------------------------------------------------------------------------------
        |                 |                     |                    |                    |
        |                 |                     |                    |                    |
        |                 |                     |                    |                    |
        |     value0      |      > value1 <     |       value2       |       value3       |
        |                 |                     |                    |                    |
        |                 |                     |                    |                    |
        -----------------------------------------------------------------------------------
                                                                                ↑ when the index reaches here you can only go backwards
        currentIndex = 1 (2nd space)
        moveSelection(3) -> currentIndex = 3
     */

    private final T[] options;
    private int currentIndex = 0;

    /**
     * Creates a carousel selection of the generic type T.
     *
     * @param options List of any type. Recommended to be three or greater.
     */
    public ArraySelect(T[] options) {
        this.options = options;
    }

    /**
     * @return Currently selected object inside the carousel.
     */
    public T getSelected() {
        return options[currentIndex];
    }

    public T[] getAllOptions() {
        return options;
    }

    /**
     * Move the index of the carousel by 1. If the index ends out of bounds of the array, it will stay at the first or last item.
     *
     * @return Returns the CarouselSelect object. Useful for chaining methods.
     */
    public ArraySelect<T> next() {
        return moveSelection(1);
    }

    /**
     * Move the index of the carousel by 1. If the index ends out of bounds of the array, it will stay at the first or last item.
     *
     * @return Returns the CarouselSelect object. Useful for chaining methods.
     */
    public ArraySelect<T> previous() {
        return moveSelection(-1);
    }

    /**
     * Move the index of the selector by a specified amount. If the index ends out of bounds of the array, it will stay at the first or last item.
     *
     * @param amount Amount to be added to the index
     * @return Returns the CarouselSelect object. Useful for chaining methods.
     */
    public ArraySelect<T> moveSelection(int amount) {
        if (currentIndex + amount >= 0 && currentIndex + amount < options.length) {
            currentIndex += amount;
        } else if (currentIndex + amount < 0) {
            currentIndex = 0;
        } else if (currentIndex + amount >= options.length) {
            currentIndex = options.length - 1;
        }
        return this;
    }

    public void setSelected(int index) {
        currentIndex = index;
    }


}
