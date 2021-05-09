package ui.windows;

import domain.WorkType;
import ui.I18N;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public final class WorkTypeDialog extends EntityDialog<WorkType> {

    private static final ui.I18N I18N = new I18N(WorkTypeDialog.class);

    private final JTextField name = new JTextField(20);
    private final JTextField hourlyRate = new JTextField(20);
    private final JTextField description = new JTextField(20);
    private Long id;

    public WorkTypeDialog() {
        add("name", name);
        add("hourlyRate", hourlyRate);
        add("description", description);
    }

    @Override
    public boolean isCorrect(StringBuilder msg) {
        if (areTextFieldsEmpty(new ArrayList<>(List.of(name, hourlyRate, description)))) {
            msg.append(I18N.getString("warningFields"));
            return false;
        }

        if (!hourlyRate.getText().matches("[0-9]+")) {
            msg.append(I18N.getString("warningRates"));
            return false;
        }
        return true;
    }

    @Override
    public WorkType getEntity() {
        var workType = new WorkType(name.getText(), Integer.parseInt(hourlyRate.getText()), description.getText());
        workType.setId(id);
        return workType;
    }

    @Override
    public void setEntity(WorkType workType) {
        id = workType.getId();
        name.setText(workType.getName());
        hourlyRate.setText(Integer.toString(workType.getRate()));
        description.setText(workType.getDescription());
    }
}
