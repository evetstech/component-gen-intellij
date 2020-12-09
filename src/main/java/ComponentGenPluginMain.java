import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class ComponentGenPluginMain extends AnAction {
    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project currentProject = event.getProject();

        if (new ComponentGenDialogWrapper(currentProject.getBasePath()).showAndGet()) {
            StringBuffer dlgMsg = new StringBuffer("Generation Complete!");

            Messages.showMessageDialog(currentProject, "Component Generated!", "Generation Complete", Messages.getInformationIcon());
        }
    }
}