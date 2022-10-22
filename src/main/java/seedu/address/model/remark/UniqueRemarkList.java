package seedu.address.model.remark;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.remark.exceptions.DuplicateRemarkException;
import seedu.address.model.remark.exceptions.RemarkNotFoundException;

/**
 * A list of Companies that enforces uniqueness between its elements and does not allow nulls.
 * A Remark is considered unique by comparing using {@code Remark#isSameRemark(Remark)}.
 * As such, adding and updating of Companies uses Remark#isSameRemark(Remark) for equality to ensure that the
 * Remark being added or updated is unique in terms of identity in the UniqueRemarkList.
 * However, the removal of a Remark uses Remark#equals(Object) to ensure that the Remark with exactly
 * the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Remark#isSameRemark(Remark)
 */
public class UniqueRemarkList implements Iterable<Remark> {

    private final ObservableList<Remark> internalList = FXCollections.observableArrayList();
    private final ObservableList<Remark> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent Remark as the given argument.
     */
    public boolean contains(Remark toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameRemark);
    }

    /**
     * Adds a Remark to the list.
     * The Remark must not already exist in the list.
     */
    public void add(Remark toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateRemarkException();
        }
        internalList.add(toAdd);
    }

    public int size() {
        return internalList.size();
    }

    /**
     * Replaces the Remark {@code target} in the list with {@code editedRemark}.
     * {@code target} must exist in the list.
     * The Remark identity of {@code editedRemark} must not be the same as another existing Remark in the list.
     */
    public void replaceRemark(Remark target, Remark editedRemark) {
        requireAllNonNull(target, editedRemark);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new RemarkNotFoundException();
        }

        if (!target.isSameRemark(editedRemark) && contains(editedRemark)) {
            throw new DuplicateRemarkException();
        }

        internalList.set(index, editedRemark);
    }

    /**
     * Removes the equivalent Remark from the list.
     * The Remark must exist in the list.
     */
    public void remove(Remark toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new RemarkNotFoundException();
        }
    }

    public Remark removeByIndex(int index) {
        return internalList.remove(index);
    }

    public void setCompanies(UniqueRemarkList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code Companies}.
     * {@code Companies} must not contain duplicate Companies.
     */
    public void setCompanies(List<Remark> companies) {
        requireAllNonNull(companies);
        if (!companiesAreUnique(companies)) {
            throw new DuplicateRemarkException();
        }

        internalList.setAll(companies);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Remark> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Remark> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueRemarkList // instanceof handles nulls
                && internalList.equals(((UniqueRemarkList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code Companies} contains only unique Companies.
     */
    private boolean companiesAreUnique(List<Remark> companies) {
        for (int i = 0; i < companies.size() - 1; i++) {
            for (int j = i + 1; j < companies.size(); j++) {
                if (companies.get(i).isSameRemark(companies.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append("Companies: ");
        Iterator<Remark> itr = this.iterator();

        String prefix = "";
        while (itr.hasNext()) {
            Remark company = itr.next();
            builder.append(prefix);
            prefix = ", ";

            builder.append(company.getName());
        }

        return builder.toString();
    }
}
