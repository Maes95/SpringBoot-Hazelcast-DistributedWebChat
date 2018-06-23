if [ "$#" -lt  "3" ]
 then
   # Use: ./deploy MyPem.pem ec2-XXX-XXX-XXX-XXX.eu-west-1.compute.amazonaws.com myJar.jar
   echo "Use: ./deploy PEM DNS JAR"
fi

PEM=$1
DNS=$2
FILE=$3

TARGET="ubuntu@${DNS}:/home/ubuntu/"

scp -i $PEM -o "StrictHostKeyChecking no" scripts/installDependencies.sh "${TARGET}installDependencies.sh"
ssh -i $PEM -o "StrictHostKeyChecking no" "ubuntu@${DNS}" chmod +x "installDependencies.sh"
ssh -i $PEM "ubuntu@${DNS}" ./installDependencies.sh

scp -i $PEM $FILE "${TARGET}Node.jar"
ssh -i $PEM "ubuntu@${DNS}" chmod +x "installDependencies.sh"
ssh -i $PEM "ubuntu@${DNS}" ./installDependencies.sh
ssh -i $PEM "ubuntu@${DNS}" "pkill -f java"
ssh -i $PEM "ubuntu@${DNS}" java -jar Node.jar
