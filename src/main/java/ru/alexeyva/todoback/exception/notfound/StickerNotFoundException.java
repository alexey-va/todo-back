package ru.alexeyva.todoback.exception.notfound;

public class StickerNotFoundException extends ElementNotFoundException {
    public StickerNotFoundException(int localId) {
        super("Sticker", localId+"");
    }
}
