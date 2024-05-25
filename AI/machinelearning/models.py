import nn

class PerceptronModel(object):
    def __init__(self, dimensions):
        """
        Initialize a new Perceptron instance.

        A perceptron classifies data points as either belonging to a particular
        class (+1) or not (-1). `dimensions` is the dimensionality of the data.
        For example, dimensions=2 would mean that the perceptron must classify
        2D points.
        """
        self.w = nn.Parameter(1, dimensions)

    def get_weights(self):
        """
        Return a Parameter instance with the current weights of the perceptron.
        """
        return self.w

    def run(self, x):
        """
        Calculates the score assigned by the perceptron to a data point x.

        Inputs:
            x: a node with shape (1 x dimensions)
        Returns: a node containing a single number (the score)
        """
        return nn.DotProduct(self.w, x)

    def get_prediction(self, x):
        """
        Calculates the predicted class for a single data point `x`.

        Returns: 1 or -1
        """
        score = nn.as_scalar(self.run(x))
        return 1 if score >= 0 else -1

    def train(self, dataset):
        """
        Train the perceptron until convergence.
        """
        
        flag = True

        while flag:
            
            flag = False

            for x, y in dataset.iterate_once(1):
                prediction = self.get_prediction(x)

                if prediction != nn.as_scalar(y):                    
                    self.w.update(x, nn.as_scalar(y))                    
                    flag = True




class RegressionModel:
    def __init__(self, input_size=20, hidden_size=20, output_size=1, learning_rate=-0.01):
        self.input_size = input_size
        self.hidden_size = hidden_size
        self.output_size = output_size
        self.learning_rate = learning_rate

        self.initialize_parameters()

    def initialize_parameters(self):
        self.weights_1 = nn.Parameter(1, self.input_size)
        self.biases_1 = nn.Parameter(1, self.input_size)

        self.weights_2 = nn.Parameter(self.input_size, self.hidden_size)
        self.biases_2 = nn.Parameter(1, self.hidden_size)

        self.weights_3 = nn.Parameter(self.hidden_size, self.output_size)
        self.biases_3 = nn.Parameter(1, self.output_size)

        self.batch_size = 0

    def run(self, x):
        if self.batch_size == 0:
            self.batch_size = x.data.shape[0]

        first_layer = nn.ReLU(nn.AddBias(nn.Linear(x, self.weights_1), self.biases_1))
        second_layer = nn.ReLU(nn.AddBias(nn.Linear(first_layer, self.weights_2), self.biases_2))
        return nn.AddBias(nn.Linear(second_layer, self.weights_3), self.biases_3)

    def get_loss(self, x, y):
        return nn.SquareLoss(self.run(x), y)

    def train(self, dataset, stopping_threshold=0.02):
        while True:
            total_loss = 0.0

            for input_data, target in dataset.iterate_once(self.batch_size):
                loss = self.get_loss(input_data, target)
                total_loss += nn.as_scalar(loss)

                parameters = [self.weights_1, self.biases_1, self.weights_2, self.biases_2, self.weights_3, self.biases_3]
                gradients = nn.gradients(loss, parameters)
                for param, gradient in zip(parameters, gradients):
                    param.update(gradient, self.learning_rate)

            average_loss = total_loss / len(dataset.x)

            if average_loss < stopping_threshold:
                break




import nn

class DigitClassificationModel:

    def __init__(self, hidden_size=200):
        self.W1 = nn.Parameter(784, hidden_size)
        self.b1 = nn.Parameter(1, hidden_size)
        self.W2 = nn.Parameter(hidden_size, 10)
        self.b2 = nn.Parameter(1, 10)

    def run(self, x):
        h1 = nn.ReLU(nn.AddBias(nn.Linear(x, self.W1), self.b1))
        scores = nn.AddBias(nn.Linear(h1, self.W2), self.b2)
        return scores

    def get_loss(self, x, y):
        return nn.SoftmaxLoss(self.run(x), y)

    def train(self, dataset, num_epochs=5, batch_size=100, learning_rate=0.3):
        for _ in range(num_epochs):
            for x_batch, y_batch in dataset.iterate_once(batch_size):
                loss = self.get_loss(x_batch, y_batch)

            
                gradients = nn.gradients(loss, [self.W1, self.b1, self.W2, self.b2])
                self.W1.update(gradients[0], -learning_rate)
                self.b1.update(gradients[1], -learning_rate)
                self.W2.update(gradients[2], -learning_rate)
                self.b2.update(gradients[3], -learning_rate)

            validation_accuracy = dataset.get_validation_accuracy()
            # print(validation_accuracy)
            if validation_accuracy >= 0.975 :
                return
            





class LanguageIDModel(object):
    def __init__(self, hidden_size=128):
        self.num_chars = 47
        self.languages = ["English", "Spanish", "Finnish", "Dutch", "Polish"]
        self.hidden_size = hidden_size

        
        self.W_initial = nn.Parameter(self.num_chars, self.hidden_size)
        self.b_initial = nn.Parameter(1, self.hidden_size)

        self.W_rnn = nn.Parameter(self.hidden_size, self.hidden_size)
        self.b_rnn = nn.Parameter(1, self.hidden_size)

        self.W_output = nn.Parameter(self.hidden_size, len(self.languages))
        self.b_output = nn.Parameter(1, len(self.languages))

    def run(self, xs):
        
        h = nn.ReLU(nn.AddBias(nn.Linear(xs[0], self.W_initial), self.b_initial))
        for x in xs[1:]:
            h = nn.ReLU(nn.AddBias(nn.Add(nn.Linear(x, self.W_initial), nn.Linear(h, self.W_rnn)), self.b_rnn))

        scores = nn.AddBias(nn.Linear(h, self.W_output), self.b_output)
        return scores

    def get_loss(self, xs, y):
        scores = self.run(xs)
        loss = nn.SoftmaxLoss(scores, y)
        return loss

    def train(self, dataset, num_epochs=5, batch_size=64, learning_rate=0.3):
        for epoch in range(num_epochs):
            total_loss = 0.0
            for xs, y in dataset.iterate_once(batch_size):
                loss = self.get_loss(xs, y)
                gradients = nn.gradients(loss, self.parameters())
                self.update_parameters(gradients, learning_rate)

                total_loss += nn.as_scalar(loss)

            # average_loss = total_loss / dataset.size
            validation_accuracy = dataset.get_validation_accuracy()
            if validation_accuracy >= 0.87:
                return

    def parameters(self):
        return [self.W_initial, self.b_initial, self.W_rnn, self.b_rnn, self.W_output, self.b_output]

    def update_parameters(self, gradients, learning_rate):
        for param, gradient in zip(self.parameters(), gradients):
            param.update(gradient, -learning_rate)