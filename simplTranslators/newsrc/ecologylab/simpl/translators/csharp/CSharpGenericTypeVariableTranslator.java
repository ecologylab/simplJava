package ecologylab.simpl.translators.csharp;

import java.util.List;

import ecologylab.generic.Debug;
import ecologylab.semantics.html.utils.StringBuilderUtils;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.GenericTypeVar;
import ecologylab.simpl.translators.DependencyTracker;
import ecologylab.translators.net.DotNetGenericsUtils;

public class CSharpGenericTypeVariableTranslator
{
  
  private DependencyTracker dependencyTracker;
  
  public String toDefinitionWithGenerics(ClassDescriptor classDescriptor)
  {
    StringBuilder sb = StringBuilderUtils.acquire();
    
    // class name, and generic type var list if any
//    sb.append(classDescriptor.getDescribedClassSimpleName());
    List<GenericTypeVar> gtvs = classDescriptor.getGenericTypeVars();
    if (gtvs != null && gtvs.size() > 0)
    {
      sb.append('<');
      for (int i = 0; i < gtvs.size(); ++i)
      {
        GenericTypeVar gtv = gtvs.get(i);
        sb.append(i == 0 ? "" : ", ").append(gtv.getName());
      }
      sb.append('>');
    }
    
    // super class
    ClassDescriptor superClassDescriptor = classDescriptor.getSuperClass();
    if (superClassDescriptor != null)
    {
      sb.append(" : ").append(toDeclarationWithGenerics(
          superClassDescriptor,
          classDescriptor.getSuperClassGenericTypeVars()));
    }
      
    // where clauses:
    if (gtvs != null && gtvs.size() > 0)
    {
      for (int i = 0; i < gtvs.size(); ++i)
      {
        GenericTypeVar gtv = gtvs.get(i);
        if (gtv.getConstraintGenericTypeVar() != null)
        {
          sb.append(" where ");
          String constraintGTVname = gtv.getConstraintGenericTypeVar().getName();
          if ("?".equals(constraintGTVname))
          {
            constraintGTVname = gtv.getConstraintGenericTypeVar()
                                   .getConstraintGenericTypeVar().getName();
          }
          sb.append(gtv.getName()).append(" : ")
            .append(constraintGTVname);
        }
        else if (gtv.getConstraintClassDescriptor() != null)
        {
          sb.append(" where ");
          sb.append(gtv.getName()).append(" : ")
            .append(toDeclarationWithGenerics(gtv.getConstraintClassDescriptor(),
                                              gtv.getConstraintGenericTypeVarArgs()));
        }
        else
        {
          Debug.warning(DotNetGenericsUtils.class, "Unprocessed generic type var: " + gtv);
        }
      }
    }
    
    String result = sb.toString();
    StringBuilderUtils.release(sb);
    return result;
  }

  public String toDeclarationWithGenerics(ClassDescriptor classDescriptor)
  {
    return toDeclarationWithGenerics(classDescriptor, classDescriptor.getGenericTypeVars());
  }

  protected String toDeclarationWithGenerics(ClassDescriptor classDescriptor,
                                                    List<GenericTypeVar> genericTypeVars)
  {
    StringBuilder sb = StringBuilderUtils.acquire();
    sb.append(classDescriptor.getDescribedClassSimpleName());
    if (genericTypeVars != null && genericTypeVars.size() > 0)
    {
      sb.append('<');
      for (int i = 0; i < genericTypeVars.size(); ++i)
      {
        GenericTypeVar gtv = genericTypeVars.get(i);
        sb.append(i == 0 ? "" : ", ");
        
        if (gtv.getClassDescriptor() != null)
        {
          sb.append(gtv.getClassDescriptor().getDescribedClassSimpleName());
        }
        else if (gtv.getName() != null)
        {
          String gtvName = gtv.getName();
          if ("?".equals(gtvName))
            gtvName = gtv.getConstraintGenericTypeVar().getName();
          sb.append(gtvName);
        }
      }
      sb.append('>');
    }
    String result = sb.toString();
    StringBuilderUtils.release(sb);
    return result;
  }
  
}
