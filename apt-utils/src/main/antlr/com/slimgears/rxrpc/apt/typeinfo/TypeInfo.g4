grammar TypeInfo;

type: (primitiveType | referenceType) EOF;

primitiveType: 'boolean' |	'byte' | 'short' | 'int' | 'long' | 'float' | 'double' | 'char';

referenceType: classOrInterfaceType | typeVariable | arrayType;

classOrInterfaceType: TypeIdentifier classOrInterfaceArgs?;

classOrInterfaceArgs: '<' typeArgument (',' typeArgument)* '>';

typeVariable: Identifier;

arrayType: (primitiveType | classOrInterfaceType | typeVariable) dim dim*;

dim: '[' ']';

typeArgument: referenceType | wildcard;

wildcard: '?' wildcardBounds?;

wildcardBounds: wildcardExtendBound | wildcardSuperBound;

wildcardSuperBound: 'super' referenceType;

wildcardExtendBound: 'extends' referenceType;

TypeIdentifier: Identifier ('.' Identifier)*;
Identifier: JavaLetter JavaLetterOrDigit*;
JavaLetter:	[a-zA-Z$_];
JavaLetterOrDigit: JavaLetter|[0-9];
WhiteSpace: [ \t\r\n\u000C]+ -> skip;
