# Makefile for Java

EXEC = IA.jar
empty :=

#Capas
C1 = empty
C2 = Experimentos

S1 = $(wildcard $(C1)/*.java)
R1 = $(subst $(C1)/,$(empty),$(S1))
S2 = $(wildcard $(C2)/*.java)
R2 = $(subst $(C2)/,$(empty),$(S2))
#S3 = $(wildcard $(C3)/*.java)
#R3 = $(subst $(C3)/,$(empty),$(S3))

CLASS1 = $(S1:.java=.class)
CLASS2 = $(S2:.java=.class)
#CLASS3 = $(S3:.java=.class)

CL1 = $(R1:.java=.class)
CL2 = $(R2:.java=.class)
#CL3 = $(R3:.java=.class)

#CLASSPATH = GraphStream/gs-ui-1.3.jar:GraphStream/gs-core-1.3.jar:GraphStream/gs-algo-1.3.jar

all: $(CLASS1) $(CLASS2)

clean:
	rm $(CL1)
	rm $(CL2)

%.class: %.java
	javac $<

#-classpath $(CLASSPATH)

print-%: ; @echo $* = $($*)

jar:
	jar cvf $(EXEC) $(CLASS1) $(CLASS2)