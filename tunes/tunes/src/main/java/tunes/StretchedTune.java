package tunes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class StretchedTune implements Tune {
    private final Tune targetTune;
    private final double factor;

    public StretchedTune(Tune targetTune, double factor) {
        this.targetTune = targetTune;
        this.factor = factor;
    }

    @NotNull
    @Override
    public List<Note> getNotes() {
        final List<Note> newNotes = new ArrayList<>();
        for (Note note : targetTune.getNotes()) {
            newNotes.add(new Note(
                    note.getPitch(),
                    Math.min(note.getDuration() * factor, 64.0)));
        }
        return newNotes;
    }

    @Override
    public void addNote(@NotNull Note note) {
        double m = Math.min(note.getDuration() / this.factor, 64.0);
        this.targetTune.addNote(new Note(note.getPitch(), m));
    }

    @Override
    public double getTotalDuration() {
        double result = 0.0;
        for (Note note : getNotes()) {
            result += note.getDuration();
        }
        return result;
    }

    @NotNull
    @Override
    public Iterator<Note> iterator() {
        return getNotes().iterator();
    }
}
