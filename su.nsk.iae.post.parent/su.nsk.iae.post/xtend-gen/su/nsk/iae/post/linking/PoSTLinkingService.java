package su.nsk.iae.post.linking;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.linking.impl.DefaultLinkingService;
import org.eclipse.xtext.linking.impl.IllegalNodeException;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class PoSTLinkingService extends DefaultLinkingService {
  @Override
  public List<EObject> getLinkedObjects(final EObject context, final EReference ref, final INode node) throws IllegalNodeException {
    final EClass requiredType = ref.getEReferenceType();
    if ((requiredType == null)) {
      return Collections.<EObject>emptyList();
    }
    final String crossRefString = this.getCrossRefNodeAsString(context, ref, node);
    if (((crossRefString == null) || crossRefString.equals(""))) {
      return Collections.<EObject>emptyList();
    }
    IScope scope = this.getScope(context, ref);
    final QualifiedName qualifiedLinkName = QualifiedName.create(crossRefString);
    IEObjectDescription eObjectDescription = this.getSingleElement(scope.getAllElements(), qualifiedLinkName);
    if ((eObjectDescription == null)) {
      return Collections.<EObject>emptyList();
    }
    final EObject result = eObjectDescription.getEObjectOrProxy();
    return Collections.<EObject>singletonList(result);
  }

  private IEObjectDescription getSingleElement(final Iterable<IEObjectDescription> elements, final QualifiedName name) {
    for (final IEObjectDescription e : elements) {
      List<String> _segments = e.getQualifiedName().getSegments();
      int _size = e.getQualifiedName().getSegments().size();
      int _minus = (_size - 1);
      String _get = _segments.get(_minus);
      String _head = IterableExtensions.<String>head(name.getSegments());
      boolean _equals = Objects.equals(_get, _head);
      if (_equals) {
        return e;
      }
    }
    return null;
  }

  private String getCrossRefNodeAsString(final EObject context, final EReference ref, final INode node) throws IllegalNodeException {
    return node.getText();
  }
}
